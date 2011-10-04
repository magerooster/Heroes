package com.herocraftonline.dev.heroes.skill.skills;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.api.HeroRegainHealthEvent;
import com.herocraftonline.dev.heroes.api.HeroesEventListener;
import com.herocraftonline.dev.heroes.classes.HeroClass;
import com.herocraftonline.dev.heroes.effects.EffectType;
import com.herocraftonline.dev.heroes.effects.PeriodicDamageEffect;
import com.herocraftonline.dev.heroes.hero.Hero;
import com.herocraftonline.dev.heroes.skill.Skill;
import com.herocraftonline.dev.heroes.skill.SkillType;
import com.herocraftonline.dev.heroes.skill.TargettedSkill;
import com.herocraftonline.dev.heroes.util.Messaging;
import com.herocraftonline.dev.heroes.util.Setting;
import com.herocraftonline.dev.heroes.util.Util;

public class SkillMortalWound extends TargettedSkill {

    private String applyText;
    private String expireText;

    public SkillMortalWound(Heroes plugin) {
        super(plugin, "MortalWound");
        setDescription("Prevents the target from healing, and applies a minor bleed effect");
        setUsage("/skill mortalwound <target>");
        setArgumentRange(0, 1);
        setIdentifiers("skill mortalwound", "skill mwound");
        setTypes(SkillType.PHYSICAL, SkillType.DAMAGING, SkillType.DEBUFF, SkillType.HARMFUL);

        registerEvent(Type.CUSTOM_EVENT, new SkillHeroListener(), Priority.Highest);
        registerEvent(Type.ENTITY_REGAIN_HEALTH, new SkillEntityListener(), Priority.Highest);
    }

    @Override
    public ConfigurationNode getDefaultConfig() {
        ConfigurationNode node = super.getDefaultConfig();
        node.setProperty("weapons", Util.swords);
        node.setProperty(Setting.DURATION.node(), 12000);
        node.setProperty(Setting.PERIOD.node(), 3000);
        node.setProperty("heal-multiplier", .5);
        node.setProperty("tick-damage", 1);
        node.setProperty(Setting.MAX_DISTANCE.node(), 2);
        node.setProperty(Setting.APPLY_TEXT.node(), "%target% has a mortal wound!");
        node.setProperty(Setting.EXPIRE_TEXT.node(), "%target% has recovered from their mortal wound!");
        return node;
    }

    @Override
    public void init() {
        super.init();
        applyText = getSetting(null, Setting.APPLY_TEXT.node(), "%target% is bleeding!").replace("%target%", "$1");
        expireText = getSetting(null, Setting.EXPIRE_TEXT.node(), "%target% has stopped bleeding!").replace("%target%", "$1");
    }

    @Override
    public boolean use(Hero hero, LivingEntity target, String[] args) {
        Player player = hero.getPlayer();

        Material item = player.getItemInHand().getType();
        if (!getSetting(hero.getHeroClass(), "weapons", Util.swords).contains(item.name())) {
            Messaging.send(player, "You can't Mortal Strike with that weapon!");
        }

        HeroClass heroClass = hero.getHeroClass();
        int damage = heroClass.getItemDamage(item) == null ? 0 : heroClass.getItemDamage(item);
        target.damage(damage, player);

        long duration = getSetting(heroClass, Setting.DURATION.node(), 12000);
        long period = getSetting(heroClass, Setting.PERIOD.node(), 3000);
        int tickDamage = getSetting(heroClass, "tick-damage", 1);
        double healMultiplier = getSetting(heroClass, "heal-multiplier", 0.5);
        MortalWound mEffect = new MortalWound(this, period, duration, tickDamage, player, healMultiplier);
        if (target instanceof Player) {
            plugin.getHeroManager().getHero((Player) target).addEffect(mEffect);
        } else if (target instanceof Creature) {
            plugin.getEffectManager().addCreatureEffect((Creature) target, mEffect);
        }

        broadcastExecuteText(hero, target);
        return true;
    }

    public class MortalWound extends PeriodicDamageEffect {

        private final double healMultiplier;

        public MortalWound(Skill skill, long period, long duration, int tickDamage, Player applier, double healMultiplier) {
            super(skill, "MortalWound", period, duration, tickDamage, applier);
            this.healMultiplier = healMultiplier;
            this.types.add(EffectType.BLEED);
        }

        @Override
        public void apply(Creature creature) {
            super.apply(creature);
        }

        @Override
        public void apply(Hero hero) {
            super.apply(hero);
            Player player = hero.getPlayer();
            broadcast(player.getLocation(), applyText, player.getDisplayName());
        }

        @Override
        public void remove(Creature creature) {
            super.remove(creature);
            broadcast(creature.getLocation(), expireText, Messaging.getCreatureName(creature).toLowerCase());
        }

        @Override
        public void remove(Hero hero) {
            super.remove(hero);

            Player player = hero.getPlayer();
            broadcast(player.getLocation(), expireText, player.getDisplayName());
        }
    }

    public class SkillEntityListener extends EntityListener {

        @Override
        public void onEntityRegainHealth(EntityRegainHealthEvent event) {
            if (event.isCancelled() || !(event.getEntity() instanceof Player))
                return;

            Hero hero = plugin.getHeroManager().getHero((Player) event.getEntity());
            if (hero.hasEffect("MortalWound")) {
                MortalWound mEffect = (MortalWound) hero.getEffect("MortalWound");
                event.setAmount((int) (event.getAmount() * mEffect.healMultiplier));
            }
        }

    }

    public class SkillHeroListener extends HeroesEventListener {

        @Override
        public void onHeroRegainHealth(HeroRegainHealthEvent event) {
            if (event.isCancelled())
                return;

            if (event.getHero().hasEffect("MortalWound")) {
                MortalWound mEffect = (MortalWound) event.getHero().getEffect("MortalWound");
                event.setAmount((int) (event.getAmount() * mEffect.healMultiplier));
            }
        }
    }
}

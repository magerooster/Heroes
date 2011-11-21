package com.herocraftonline.dev.heroes.skill.skills;

import com.herocraftonline.dev.heroes.skill.SkillType;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.effects.common.SlowEffect;
import com.herocraftonline.dev.heroes.hero.Hero;
import com.herocraftonline.dev.heroes.skill.TargettedSkill;
import com.herocraftonline.dev.heroes.util.Setting;

public class SkillSlow extends TargettedSkill {
    
    private String applyText;
    private String expireText;
    
    public SkillSlow(Heroes plugin) {
        super(plugin, "Slow");
        setDescription("Slows the target's movement speed & attack speed");
        setUsage("/skill slow");
        setArgumentRange(0, 1);
        setIdentifiers("skill slow");
        setTypes(SkillType.DEBUFF, SkillType.MOVEMENT, SkillType.SILENCABLE, SkillType.HARMFUL);
    }

    @Override
    public ConfigurationNode getDefaultConfig() {
        ConfigurationNode node = super.getDefaultConfig();
        node.setProperty("speed-multiplier", 2);
        node.setProperty(Setting.DURATION.node(), 15000);
        node.setProperty(Setting.APPLY_TEXT.node(), "%target% has been slowed by %hero%!");
        node.setProperty(Setting.EXPIRE_TEXT.node(), "%target% is no longer slowed!");
        return node;
    }


    @Override
    public void init() {
        applyText = getSetting(null, Setting.APPLY_TEXT.node(), "%target% has been slowed by %hero%!").replace("%target%", "$1").replace("%hero%", "$2");
        expireText = getSetting(null, Setting.EXPIRE_TEXT.node(), "%target% is no longer slowed!").replace("%target%", "$1");
    }

    @Override
    public SkillResult use(Hero hero, LivingEntity target, String[] args) {
        if (!(target instanceof Player))
            return SkillResult.INVALID_TARGET;
        
        int duration = getSetting(hero, Setting.DURATION.node(), 15000, false);
        int multiplier = getSetting(hero, "speed-multiplier", 2, false);
        if (multiplier > 20) {
            multiplier = 20;
        }
        SlowEffect effect = new SlowEffect(this, duration, multiplier, true, applyText, expireText, hero);
        plugin.getHeroManager().getHero((Player) target).addEffect(effect);
        return SkillResult.NORMAL;
    }
}
package com.herocraftonline.dev.heroes.command.skill.skills;

import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.command.skill.ActiveSkill;
import com.herocraftonline.dev.heroes.persistence.Hero;

public class SkillSummon extends ActiveSkill {

    public SkillSummon(Heroes plugin) {
        super(plugin);
        name = "Summon";
        description = "Skill - Summon";
        usage = "/summon <monster>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("summon");
    }

    @Override
    public boolean use(Hero hero, String[] args) {
        Player player = hero.getPlayer();
        CreatureType creatureType = CreatureType.fromName(args[0].toUpperCase());
        if (creatureType != null && hero.getSummons().size() <= hero.getPlayerClass().getSummonMax()) {
            Entity spawnedEntity = player.getWorld().spawnCreature(player.getLocation(), creatureType);
            if (spawnedEntity instanceof Creature && spawnedEntity instanceof Ghast && spawnedEntity instanceof Slime) {
                spawnedEntity.remove();
                return false;
            }
            hero.getSummons().put(spawnedEntity, creatureType);
            plugin.getMessager().send(player, "You have succesfully summoned a " + creatureType.toString());
            return true;
        }
        return false;
    }

    public class SkillEntityListener extends EntityListener {

        public void onEntityTarget(EntityTargetEvent event) {
            if (event.getTarget() instanceof Player) {
                for (Hero hero : plugin.getHeroManager().getHeroes()) {
                    if (hero.getSummons().containsKey(event.getEntity())) {
                        if (hero.getPlayer() == event.getTarget()) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

        @Override
        public void onEntityDeath(EntityDeathEvent event) {
            Entity defender = event.getEntity();
            for (Hero hero : plugin.getHeroManager().getHeroes()) {
                if (hero.getSummons().containsKey(defender)) {
                    hero.getSummons().remove(defender);
                }
            }

        }

    }
}
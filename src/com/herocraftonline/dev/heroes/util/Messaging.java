package com.herocraftonline.dev.heroes.util;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.classes.HeroClass;
import com.herocraftonline.dev.heroes.hero.Hero;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public final class Messaging {

    public static void broadcast(Heroes plugin, String msg, Object... params) {
        plugin.getServer().broadcastMessage(parameterizeMessage(msg, params));
    }

    public static String createFullHealthBar(double health, double maxHealth) {
        return "§aHP: §f" + (int) Math.ceil(health) + "/" + (int) Math.ceil(maxHealth) + " " + createHealthBar(health, maxHealth);
    }

    public static String createHealthBar(double health, double maxHealth) {
        StringBuilder healthBar = new StringBuilder(ChatColor.RED + "[" + ChatColor.GREEN);
        int progress = (int) (health / maxHealth * 50.0);
        for (int i = 0; i < progress; i++) {
            healthBar.append('|');
        }
        healthBar.append(ChatColor.DARK_RED);
        for (int i = 0; i < 50 - progress; i++) {
            healthBar.append('|');
        }
        healthBar.append(ChatColor.RED).append(']');
        return healthBar + " - " + ChatColor.GREEN + (int) (health / maxHealth * 100.0) + "%";
    }

    public static String createManaBar(int mana) {
        StringBuilder manaBar = new StringBuilder(ChatColor.RED + "[" + ChatColor.BLUE);
        int progress = (int) (mana / 100.0 * 50);
        for (int i = 0; i < progress; i++) {
            manaBar.append('|');
        }
        manaBar.append(ChatColor.DARK_RED);
        for (int i = 0; i < 50 - progress; i++) {
            manaBar.append('|');
        }
        manaBar.append(ChatColor.RED).append(']');
        return manaBar + " - " + ChatColor.BLUE + mana + "%";
    }

    public static String createExperienceBar(int exp, int currentLevelExp, int nextLevelExp) {
        StringBuilder expBar = new StringBuilder(ChatColor.RED + "[" + ChatColor.DARK_GREEN);
        int progress = (int) ((double) (exp - currentLevelExp) / (nextLevelExp - currentLevelExp) * 50);
        for (int i = 0; i < progress; i++) {
            expBar.append('|');
        }
        expBar.append(ChatColor.DARK_RED);
        for (int i = 0; i < 50 - progress; i++) {
            expBar.append('|');
        }
        expBar.append(ChatColor.RED + "]");
        expBar.append(" - " + ChatColor.DARK_GREEN + progress * 2 + "%  ");
        expBar.append("" + ChatColor.DARK_GREEN + (exp - currentLevelExp) + ChatColor.RED + "/" + ChatColor.DARK_GREEN + (nextLevelExp - currentLevelExp));
        return expBar.toString();
    }

    public static String createExperienceBar(Hero hero, HeroClass heroClass) {
        int level = hero.getLevel(heroClass);
        return createExperienceBar((int) hero.getExperience(heroClass), Properties.getTotalExp(level), Properties.getTotalExp(level + 1));
    }

    public static String getLivingEntityName(LivingEntity lEntity) {
        if (lEntity instanceof Player) {
            return ((Player) lEntity).getDisplayName();
        } else if (lEntity instanceof Blaze) {
            return "Blaze";
        } else if (lEntity instanceof CaveSpider) {
            return "Cave Spider";
        } else if (lEntity instanceof MushroomCow) {
            return "Mushroom Cow"; // Must resolve before Cow as it extends Cow
        } else if (lEntity instanceof Cow) {
            return "Cow";
        } else if (lEntity instanceof Chicken) {
            return "Chicken";
        } else if (lEntity instanceof Creeper) {
            return "Creeper";
        } else if (lEntity instanceof EnderDragon) {
            return "Ender Dragon";
        } else if (lEntity instanceof Enderman) {
            return "Enderman";
        } else if (lEntity instanceof Ghast) {
            return "Ghast";
        } else if (lEntity instanceof Giant) {
            return "Giant";
        } else if (lEntity instanceof MagmaCube) {
            return "Magma Cube";
        } else if (lEntity instanceof Pig) {
            return "Pig";
        } else if (lEntity instanceof PigZombie) {
            return "Pig Zombie";
        } else if (lEntity instanceof Sheep) {
            return "Sheep";
        } else if (lEntity instanceof Skeleton) {
            return "Skeleton";
        } else if (lEntity instanceof Silverfish) {
            return "Silverfish";
        } else if (lEntity instanceof Slime) {
            return "Slime";
        } else if (lEntity instanceof Snowman) {
            return "Snowman";
        } else if (lEntity instanceof Spider) {
            return "Spider";
        } else if (lEntity instanceof Squid) {
            return "Squid";
        } else if (lEntity instanceof Wolf) {
            return "Wolf";
        } else if (lEntity instanceof Villager) {
            return "Villager";
        } else if (lEntity instanceof Zombie) {
            return "Zombie";
        } else {
            return null;
        }
    }

    public static void send(CommandSender player, String msg, Object... params) {
        player.sendMessage(parameterizeMessage(msg, params));
    }

    public static String parameterizeMessage(String msg, Object... params) {
        msg = ChatColor.GRAY + msg;
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                msg = msg.replace("$" + (i + 1), ChatColor.WHITE + params[i].toString() + ChatColor.GRAY);
            }
        }
        return msg;
    }
}

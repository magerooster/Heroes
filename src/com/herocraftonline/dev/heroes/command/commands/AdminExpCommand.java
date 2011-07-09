package com.herocraftonline.dev.heroes.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.command.BaseCommand;
import com.herocraftonline.dev.heroes.persistence.Hero;
import com.herocraftonline.dev.heroes.util.Messaging;

public class AdminExpCommand extends BaseCommand {

    public AdminExpCommand(Heroes plugin) {
        super(plugin);
        setName("AdminExpCommand");
        setDescription("Changes a users exp");
        setUsage("/hero admin exp §9<player> <exp>");
        setMinArgs(2);
        setMaxArgs(2);
        getIdentifiers().add("hero admin exp");
        setPermissionNode("heroes.admin.exp");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = plugin.getServer().getPlayer(args[0]);
        Hero hero = plugin.getHeroManager().getHero(player);
        // Check the Player exists.
        if (player == null) {
            Messaging.send(sender, "Failed to find a matching Player for '$1'.", args[0]);
            return;
        }
        hero.setExperience(Integer.parseInt(args[1]));
    }
}

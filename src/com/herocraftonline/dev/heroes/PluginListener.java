package com.herocraftonline.dev.heroes;

import org.bukkit.event.server.PluginEvent;
import org.bukkit.event.server.ServerListener;

import com.nijiko.coelho.iConomy.iConomy;
import org.bukkit.plugin.Plugin;

/**
 * Checks for plugins whenever one is enabled
 */
public class PluginListener extends ServerListener {
    public PluginListener() { }

    @Override
    public void onPluginEnabled(PluginEvent event) {
        if(Heroes.getiConomy() == null) {
            Plugin iConomy = Heroes.getBukkitServer().getPluginManager().getPlugin("iConomy");

            if (iConomy != null) {
                if(iConomy.isEnabled()) {
                    Heroes.setiConomy((iConomy)iConomy);
                    System.out.println("[Heroes] Successfully linked with iConomy.");
                }
            }
        }
    }
}


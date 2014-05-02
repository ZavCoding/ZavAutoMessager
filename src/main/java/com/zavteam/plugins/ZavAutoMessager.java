package com.zavteam.plugins;

import com.zavteam.plugins.packets.AutoPacket;
import com.zavteam.plugins.packets.CommandPacket;
import com.zavteam.plugins.packets.MessagePacket;
import com.zavteam.plugins.utils.CustomConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Main running class. This class should be responsible only for running the plugin.
 * Individual responsibilities should fall on separate classes.
 * Not doing the aforementioned is what made this re-write required.
 * Keep code as modular as possible.
 */
public class ZavAutoMessager extends JavaPlugin {

    List<AutoPacket> autoPacketList = new ArrayList<AutoPacket>();

    CustomConfig mainConfig = new CustomConfig(this, "config.yml");
    CustomConfig ignoreConfig = new CustomConfig(this, "ignore.yml");

    public void onEnable() {
        mainConfig.reloadConfig();
        ignoreConfig.reloadConfig();
        loadMessages();
    }

    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoPacketRunnable(), 0L, ((long) mainConfig.getConfig().getInt("delay") * 20));
    }

    public void loadMessages() {
        for (String permission : mainConfig.getConfig().getConfigurationSection("messages").getKeys(false)) {
            for (String message : mainConfig.getConfig().getStringList("messages." + permission)) {
                AutoPacket autoPacket = null;
                if (message.startsWith("/")) {
                    autoPacket = new CommandPacket(message.substring(1));
                } else {
                    autoPacket = new MessagePacket(message, permission);
                    ((MessagePacket) autoPacket).processMessages(mainConfig.getConfig().getBoolean("wordwrap"));
                }
                autoPacketList.add(autoPacket);
            }
        }
    }

}

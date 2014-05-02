package com.zavteam.plugins;

import com.zavteam.plugins.packets.AutoPacket;
import com.zavteam.plugins.packets.CommandPacket;
import com.zavteam.plugins.packets.MessagePacket;
import com.zavteam.plugins.utils.CustomConfig;
import com.zavteam.plugins.utils.PluginPM;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Main running class. This class should be responsible only for running the plugin.
 * Individual responsibilities should fall on separate classes.
 * Not doing the aforementioned is what made this re-write required.
 * Keep code as modular as possible.
 */
public class ZavAutoMessager extends JavaPlugin {

    List<AutoPacket> autoPacketList = new ArrayList<AutoPacket>();

    public static CustomConfig getMainConfig() {
        return mainConfig;
    }

    public static CustomConfig getIgnoreConfig() {
        return ignoreConfig;
    }

    private static CustomConfig mainConfig;
    private  static CustomConfig ignoreConfig;

    public void onEnable() {
        mainConfig = new CustomConfig(this, "config.yml");
        ignoreConfig = new CustomConfig(this, "ignore.yml");
        mainConfig.saveDefaultConfig();
        ignoreConfig.saveDefaultConfig();
        mainConfig.reloadConfig();
        ignoreConfig.reloadConfig();
        loadMessages();
        if (autoPacketList.size() < 1) {
            PluginPM.sendMessage(Level.SEVERE, "No messages could be loaded. Disabling plugin.");
            setEnabled(false);
            return;
        }
        getServer().getScheduler().cancelTasks(this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoPacketRunnable(this), 0L, ((long) mainConfig.getConfig().getInt("delay") * 20));
    }

    public void onDisable() {

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

    public List<AutoPacket> getAutoPacketList() {
        return autoPacketList;
    }

}

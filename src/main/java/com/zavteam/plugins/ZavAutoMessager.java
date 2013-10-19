package com.zavteam.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.zavteam.plugins.messageshandler.MessagesHandler;
import com.zavteam.plugins.utils.Config;
import com.zavteam.plugins.configs.VersionConfig;

public class ZavAutoMessager extends JavaPlugin {

	public List<ChatMessage> messages = new ArrayList<ChatMessage>();

	public static Logger log;

	int messageIt;

	RunnableMessager RunnableMessager = new RunnableMessager(this);
	
	MessagesHandler MessagesHandler = new MessagesHandler(this);
	
	VersionConfig VersionConfig = new VersionConfig(this);
	
	public Config mainConfig;
	public Config ignoreConfig;

	@Override
	public void onDisable() {
		log.info(this + " has been disabled");

	}

	@Override
	public void onEnable() {
		mainConfig = new Config(this, "config.yml");
		ignoreConfig = new Config(this, "ignore.yml");
		log = getServer().getLogger();
		try {
			autoReload();
			
		} catch (NullPointerException npe) {
			log.severe(this + " has encountered a sever error. No messages are in the config");
			log.severe(this + " If you are updating from a version 2.2 or below please update your config to the new layout");
		}
		VersionConfig.loadConfig();
		Commands commands = new Commands(this);
		getCommand("automessager").setExecutor(commands);
		getCommand("am").setExecutor(commands);
		getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onPlayerJoin(PlayerJoinEvent event) {
				if (mainConfig.getConfig().getBoolean("updatechecking")) {
					Player p = event.getPlayer();
					if (!(getDescription().getVersion().equals(VersionConfig.getVersion()))) {
						if (p.isOp()) {
							p.sendMessage(ChatColor.GOLD + "A new version of ZavAutoMessager is available. Use \"/am about\" for more info.");
						}
					}
				}
			}
		}, this);
		log.info(this + " has been enabled");
		log.info(this + ": Sending messages is now set to " + mainConfig.getConfig().getBoolean("enabled"));
		if (!(getDescription().getVersion().equals(VersionConfig.getVersion()))) {
			log.info(this + " is not up to date. Check the latest version on BukkitDev.");
			log.info(this + " The latest version is currently " + VersionConfig.getVersion());
		} else {
			log.info(this + " is up to date!");
		}
		log.info("Thank you for using " + this + " by the ZavTeam!");
	}
	
	public void autoReload() {
		mainConfig.loadConfig();
		ignoreConfig.loadConfig();
		messages = getMessages();
		getServer().getScheduler().cancelTasks(this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, RunnableMessager, 0L, ((long) mainConfig.getConfig().getInt("delay") * 20));
	}
	
	
	public void disableZavAutoMessager() {
		setEnabled(false);
	}
	
	public List<ChatMessage> getMessages() {
		List<ChatMessage> messages = new ArrayList<ChatMessage>();
		try {
			for (String permission : mainConfig.getConfig().getConfigurationSection("messages").getKeys(false)) {
				for (String message : mainConfig.getConfig().getStringList("messages." + permission)) {
					ChatMessage cm = new ChatMessage(null, permission);
					if (message.startsWith("/")) {
						message = message.substring(1);
						cm.setMessage(message);
						cm.setCommand(true);
					} else {
						cm.setMessage(message);
						cm.setCommand(false);
					}
					messages.add(cm);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.severe(this + " has encountered a sever error. No messages are in the config");
			log.severe(this + " If you are updating from a version 2.2 or below please update your config to the new layout");
		}
		return messages;
	}
	
}
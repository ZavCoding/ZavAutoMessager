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

import com.zavteam.plugins.configs.IgnoreConfig;
import com.zavteam.plugins.configs.MainConfig;
import com.zavteam.plugins.configs.VersionConfig;

public class ZavAutoMessager extends JavaPlugin {

	public List<ChatMessage> messages = new ArrayList<ChatMessage>();

	public static Logger log;

	int messageIt;

	RunnableMessager runnableMessager = new RunnableMessager(this);

	@Override
	public void onDisable() {
		log.info(this + " has been disabled");

	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		log = getServer().getLogger();
		try {
			autoReload();
		} catch (NullPointerException npe) {
			log.severe(this + " has encountered a sever error. No messages are in the config");
			log.severe(this + " If you are updating from a version 2.2 or below please update your config to the new layout");
		}
		VersionConfig.loadConfig();
		IgnoreConfig.loadConfig();
		Commands commands = new Commands(this);
		getCommand("automessager").setExecutor(commands);
		getCommand("am").setExecutor(commands);
		getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onPlayerJoin(PlayerJoinEvent event) {
				if (MainConfig.getUpdateChecking()) {
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
		log.info(this + ": Sending messages is now set to " + MainConfig.getEnabled());
		if (!(getDescription().getVersion().equals(VersionConfig.getVersion()))) {
			log.info(this + " is not up to date. Check the latest version on BukkitDev.");
			log.info(this + " The latest version is currently " + VersionConfig.getVersion());
		} else {
			log.info(this + " is up to date!");
		}
		log.info("Thank you for using " + this + " by the ZavTeam!");
	}
	public void autoReload() {
		MainConfig.loadConfig();
		getServer().getScheduler().cancelTasks(this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, runnableMessager, 0L, ((long) MainConfig.getDelay() * 20));
	}
	
	public void disableZavAutoMessager() {
		setEnabled(false);
	}
}
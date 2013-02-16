package com.zavteam.plugins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.zavteam.plugins.configs.IgnoreConfig;
import com.zavteam.plugins.configs.MainConfig;
import com.zavteam.plugins.configs.VersionConfig;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	public List<ChatMessage> messages = new ArrayList<ChatMessage>();
	
	public Logger log;
	
	int messageIt;
	
	RunnableMessager rm = new RunnableMessager();
	
	@Override
	public void onDisable() {
		log.info(this + " has been disabled");

	}

	@Override
	public void onEnable() {
		plugin = this;
		try {
		    BukkitMetrics metrics = new BukkitMetrics(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
		saveDefaultConfig();
		log = getServer().getLogger();
		autoReload();
		messages = MainConfig.getMessages();
		VersionConfig.loadConfig();
		IgnoreConfig.loadConfig();
		Commands commands = new Commands(this);
		getCommand("automessager").setExecutor(commands);
		getCommand("am").setExecutor(commands);
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
		getServer().getScheduler().scheduleSyncRepeatingTask(this, rm, 0L, ((long) MainConfig.getDelay() * 20));
	}
	public void disableZavAutoMessager() {
		setEnabled(false);
	}
}
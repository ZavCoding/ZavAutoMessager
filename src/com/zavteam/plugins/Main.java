package com.zavteam.plugins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.zavteam.plugins.configs.MainConfig;
import com.zavteam.plugins.configs.VersionConfig;
import com.zavteam.plugins.messageshandler.MessagesHandler;

public class Main extends JavaPlugin {
	
	public List<String> messages = new ArrayList<String>();
	
	public List<String> ignorePlayers = new ArrayList<String>();
	
	public Logger log;
	
	int messageIt;
	
	RunnableMessager rm = new RunnableMessager(this);
	
	public MainConfig MConfig = new MainConfig(this);
	
	public MessagesHandler MHandler = new MessagesHandler(this);
	
	public VersionConfig VConfig = new VersionConfig(this);
	
	@Override
	public void onDisable() {
		log.info(this + " has been disabled");

	}

	@Override
	public void onEnable() {
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
		saveDefaultConfig();
		log = getServer().getLogger();
		autoReload();
		messages = MConfig.getMessages();
		VConfig.loadConfig();
		Commands commands = new Commands(this);
		getCommand("automessager").setExecutor(commands);
		getCommand("am").setExecutor(commands);
		log.info(this + " has been enabled");
		log.info(this + ": Sending messages is now set to " + MConfig.getEnabled());
		if (!(getDescription().getVersion().equals(VConfig.getVersion()))) {
			log.info(this + " is not up to date. Check the latest version on BukkitDev.");
			log.info(this + " The latest version is currently " + VConfig.getVersion());
		} else {
			log.info(this + " is up to date!");
		}
		log.info("Thank you for using " + this + " by the ZavTeam!");
	}
	public void autoReload() {
		MConfig.loadConfig();
		getServer().getScheduler().cancelTasks(this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, rm, 0L, ((long) MConfig.getDelay() * 20));
	}
	public void disableZavAutoMessager() {
		setEnabled(false);
	}
}
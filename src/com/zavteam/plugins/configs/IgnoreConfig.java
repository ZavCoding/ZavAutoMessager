package com.zavteam.plugins.configs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.zavteam.plugins.Main;

public class IgnoreConfig {
	public Main plugin;
	public IgnoreConfig(Main instance) {
		plugin = instance;
	}
	File ignoreFile;
	private FileConfiguration config;
	public void loadConfig() {
		InputStream defaultIgnoreConfigStream;
		if (ignoreFile == null) {
			ignoreFile = new File(plugin.getDataFolder(), "ignore.yml");
		}
		config = YamlConfiguration.loadConfiguration(ignoreFile);
		defaultIgnoreConfigStream = plugin.getResource("ignore.yml");
		if (defaultIgnoreConfigStream != null) {
			config.setDefaults(YamlConfiguration.loadConfiguration(defaultIgnoreConfigStream));
		}
		saveConfig();
	}
	public void saveConfig() {
		if (config == null || ignoreFile == null) {
			plugin.log.severe(this + " is unable to save the config.");
			return;
		}
		try {
			config.save(ignoreFile);
		} catch (IOException e) {
			e.printStackTrace();
			plugin.log.severe(this + " is unable to save config.");
		}
	}
	public List<String> getIgnorePlayers() {
		return config.getStringList("players");
	}
	public void set(String s, Object o) {
		config.set(s, o);
		saveConfig();
	}
}

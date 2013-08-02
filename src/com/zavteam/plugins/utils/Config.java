package com.zavteam.plugins.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class Config {

	private JavaPlugin plugin;
	private String fileName;
	private FileConfiguration configFile;

	public Config(JavaPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		this.loadConfig();
	}
	
	/**
	 * Loads and saves configs with defaults if needed
	 */
	public void loadConfig() {
		File file = new File(plugin.getDataFolder(), fileName);
		if (file.exists())
		{
			configFile = YamlConfiguration.loadConfiguration(file);
		}
		else
		{
			InputStream defaultsStream = plugin.getResource(fileName);
			if (defaultsStream != null) {
				configFile = YamlConfiguration.loadConfiguration(defaultsStream);
			}
		}
		saveConfig();
	}
	
	/**
	 * 
	 * @return Name of file including extension
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * 
	 * @return The file configuration for this wrapper
	 */
	public FileConfiguration getConfig() {
		if (configFile == null) {
			loadConfig();
		}
		return configFile;
	}
	
	/**
	 * Saves file
	 */
	public void saveConfig() {
		File file = new File(plugin.getDataFolder(), fileName);
		try {
			configFile.save(file);
		} catch (IOException e) {
			plugin.getLogger().severe("Could not save config. Printing stack trace: ");
			e.printStackTrace();
		}
	}

}

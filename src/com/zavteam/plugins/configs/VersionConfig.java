package com.zavteam.plugins.configs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.zavteam.plugins.ZavAutoMessager;

public class VersionConfig {
	
	ZavAutoMessager plugin;
	
	public VersionConfig(ZavAutoMessager plugin) {
		this.plugin = plugin;
	}
	
	private FileConfiguration config;
	public void loadConfig() {
		if (!MainConfig.getUpdateChecking()) {
			return;
		}
		BufferedInputStream versionConfigStream = null;
		try {
			versionConfigStream = new BufferedInputStream(new URL("https://sites.google.com/site/zachoooo/version.yml").openStream());
		} catch (MalformedURLException e) {
			ZavAutoMessager.log.warning("Please Contact the developer regarding this error.");
			e.printStackTrace();
		} catch (IOException e) {
			ZavAutoMessager.log.warning("There is a problem with your internet connection. Cannot get current version.");
			config = null;
		} catch (Exception e) {
			ZavAutoMessager.log.warning("You may have a problem with your internet. Could not acquire version D:!");
			config = null;
		}
		if (versionConfigStream != null) {
			config = YamlConfiguration.loadConfiguration(versionConfigStream);
		} else {
			ZavAutoMessager.log.warning(plugin + " was unable to retrieve current version.");
		}	
	}
	public String getVersion() {
		if (config == null) {
			return "-Update Checking Disabled-";
		}
		return config.getString("version");
	}
}

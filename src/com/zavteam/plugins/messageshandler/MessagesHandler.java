package com.zavteam.plugins.messageshandler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zavteam.plugins.ChatMessage;
import com.zavteam.plugins.ZavAutoMessager;
import com.zavteam.plugins.API.MessageBroadcastEvent;

public class MessagesHandler {
	
	public ZavAutoMessager plugin;
	
	public MessagesHandler(ZavAutoMessager plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * 
	 * @param sarray The array of strings to send and handle and send to the users
	 * @param cm The chat message that they originated from. Can be null. Is only used for permissions. Fix that.
	 */
	public void handleChatMessage(String[] sarray, @Nullable ChatMessage cm) {
		
		boolean permissionsBV = (Boolean) plugin.mainConfig.get("permissionsenabled", false);
		
		List<String> players = new ArrayList<String>();
		
		if (plugin.getServer().getOnlinePlayers().length == 0 && (Boolean) plugin.mainConfig.get("requireplayersonline", true)) {
			return;
		}
		if (permissionsBV || !(cm == null)) {
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				if ((player.hasPermission(cm.getPermission()) || cm.getPermission().equalsIgnoreCase("default")) && !plugin.ignoreConfig.getConfig().getStringList("players").contains(player.getName())) {
					players.add(player.getName());
				}
			}
		} else {
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				if (!plugin.ignoreConfig.getConfig().getStringList("players").contains(player.getName())) {
					players.add(player.getName());
				}
			}
		}
		
		MessageBroadcastEvent mbe = new MessageBroadcastEvent(sarray, players);
		Bukkit.getPluginManager().callEvent(mbe);
		if (mbe.isCancelled())
			return;
		for (String s : mbe.getPlayers()) {
			Bukkit.getPlayer(s).sendMessage(mbe.getMessage());
		}
		if ((Boolean) plugin.mainConfig.get("messagesinconsole", true)) {
			Bukkit.getConsoleSender().sendMessage(mbe.getMessage());
		}
	}
	
	/**
	 * 
	 * @param m The message to add to the list of commands
	 */
	public void addMessage(String m) {
		List<String> s = plugin.mainConfig.getConfig().getStringList("messages.default");
		s.add(m);
		plugin.mainConfig.set("messages.default", s);
		plugin.mainConfig.saveConfig();
		plugin.mainConfig.loadConfig();
		plugin.messages = plugin.getMessages();
	}
	
	/**
	 * 
	 * @param permission The permission node under which the message should be added
	 * @param m
	 */
	public void addMessage(String permission, String m) {
		String path = "messages." + permission;
		List<String> s = plugin.mainConfig.getConfig().getStringList(path);
		s.add(m);
		plugin.mainConfig.set(path, m);
	}
	
	public void listPage(int i, CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "ZavAutoMessager Messages Page: " + i);
		int initialInt = (5 * i) - 5;
		int finalInt = initialInt + 5;
		for (int iterator = initialInt; iterator < finalInt; iterator++) {
			String message = ChatColor.GOLD + Integer.toString(iterator + 1) + ". ";
			try {
				ChatMessage cm = plugin.messages.get(iterator);
				message = message + "Node: " + cm.getPermission() + " Message: " + cm.getMessage();
				message = message + ChatColor.translateAlternateColorCodes('&', plugin.messages.get(iterator).getMessage());
			} catch (IndexOutOfBoundsException e) {
				message = message + "None";
			}
			sender.sendMessage(message);
		}
	}
	public static void listHelpPage(int i, CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "========= ZavAutoMessager Help =========");
		switch (i) {
		case 1: {
			sender.sendMessage(ChatColor.GOLD + "1. /automessager reload - Reloads config");
			sender.sendMessage(ChatColor.GOLD + "2. /automessager on - Start the messages");
			sender.sendMessage(ChatColor.GOLD + "3. /automessager off - Stops the messages");
			sender.sendMessage(ChatColor.GOLD + "4. /automessager add <message> - Adds a message to the list");
			sender.sendMessage(ChatColor.GOLD + "5. /automessager remove <message number> - Removes message from list");
			sender.sendMessage(ChatColor.GOLD + "=============== Page 1/3 ===============");
			break;
		}
		case 2: {
			sender.sendMessage(ChatColor.GOLD + "6. /automessager ignore - Toggles ignoring messages");
			sender.sendMessage(ChatColor.GOLD + "7. /automessager broadcast <message> - Send a message now");
			sender.sendMessage(ChatColor.GOLD + "8. /automessager about - Displays info about the Main.plugin");
			sender.sendMessage(ChatColor.GOLD + "9. /automessage list (page) - Shows a message list");
			sender.sendMessage(ChatColor.GOLD + "10. /automessager help (page)- Displays this menu");
			sender.sendMessage(ChatColor.GOLD + "=============== Page 2/3 ===============");
			break;
		}
		case 3: {
			sender.sendMessage(ChatColor.GOLD + "11. /automessager set <config section/list> <value>");
			sender.sendMessage(ChatColor.GOLD + "12.");
			sender.sendMessage(ChatColor.GOLD + "13.");
			sender.sendMessage(ChatColor.GOLD + "14.");
			sender.sendMessage(ChatColor.GOLD + "15.");
			sender.sendMessage(ChatColor.GOLD + "=============== Page 3/3 ===============");
		}
		}
	}
}
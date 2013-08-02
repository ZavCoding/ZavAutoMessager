package com.zavteam.plugins.messageshandler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zavteam.plugins.ChatMessage;
import com.zavteam.plugins.ZavAutoMessager;
import com.zavteam.plugins.API.MessageBroadcastEvent;
import com.zavteam.plugins.configs.IgnoreConfig;
import com.zavteam.plugins.configs.MainConfig;

public class MessagesHandler {
	public static void handleMessage(String[] sarray, ChatMessage cm) {
		
		boolean permissionsBV = MainConfig.getPermissionEnabled();
		
		List<String> players = new ArrayList<String>();
		
		if (ZavAutoMessager.plugin.getServer().getOnlinePlayers().length == 0 && MainConfig.getRequirePlayers()) {
			return;
		}
		if (permissionsBV || !(cm == null)) {
			for (Player player : ZavAutoMessager.plugin.getServer().getOnlinePlayers()) {
				if ((player.hasPermission(cm.getPermission()) || cm.getPermission().equalsIgnoreCase("default")) && !IgnoreConfig.getIgnorePlayers().contains(player.getName())) {
					players.add(player.getName());
				}
			}
		} else {
			for (Player player : ZavAutoMessager.plugin.getServer().getOnlinePlayers()) {
				if (!IgnoreConfig.getIgnorePlayers().contains(player.getName())) {
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
		if (MainConfig.getMessagesInConsole()) {
			Bukkit.getConsoleSender().sendMessage(mbe.getMessage());
		}
	}

	public static void addMessage(String m) {
		List<String> s = MainConfig.config.getStringList("messages.default");
		s.add(m);
		MainConfig.set("messages.default", s);
	}
	
	public static void addMessage(String permission, String m) {
		String path = "messages." + permission;
		List<String> s = MainConfig.config.getStringList(path);
		s.add(m);
		MainConfig.set(path, m);
	}
	
	public static void listPage(int i, CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "ZavAutoMessager Messages Page: " + i);
		int initialInt = (5 * i) - 5;
		int finalInt = initialInt + 5;
		for (int iterator = initialInt; iterator < finalInt; iterator++) {
			String message = ChatColor.GOLD + Integer.toString(iterator + 1) + ". ";
			try {
				ChatMessage cm = ZavAutoMessager.plugin.messages.get(iterator);
				message = message + "Node: " + cm.getPermission() + " Message: " + cm.getMessage();
				message = message + ChatColor.translateAlternateColorCodes('&', ZavAutoMessager.plugin.messages.get(iterator).getMessage());
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
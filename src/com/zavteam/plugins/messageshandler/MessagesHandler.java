package com.zavteam.plugins.messageshandler;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zavteam.plugins.Main;

public class MessagesHandler {
	public Main plugin;
	public MessagesHandler(Main instance) {
		plugin = instance;
	}
	public void handleMessage(String[] sarray) {
		boolean permissionsBV = plugin.MConfig.getPermissionEnabled();
		if (plugin.getServer().getOnlinePlayers().length == 0) {
			return;
		}
		if (permissionsBV) {
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				if (player.hasPermission("zavautomessager.see") || !(plugin.IConfig.getIgnorePlayers().contains(player.getName()))) {
					player.sendMessage(sarray);
				}
			}
		} else {
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				if (!plugin.IConfig.getIgnorePlayers().contains(player.getName())) {
					player.sendMessage(sarray);
				}
			}
		}
		if (plugin.MConfig.getMessagesInConsole()) {
			for (String s : sarray) {
				plugin.log.info(s);	
			}
		}
	}
	public void addMessage(String m) {
		plugin.messages.add(m);
		plugin.MConfig.set("messages", plugin.messages);
	}
	public void listPage(int i, CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "ZavAutoMessager Messages Page: " + i);
		int pagenumber = (i-1) * 5;
		for (int i2 = pagenumber; i2 > (pagenumber + 4); i2++) {
			sender.sendMessage(ChatColor.GOLD + String.valueOf(i2 + 1) + ". " + plugin.messages.get(i2));
		}
	}
	public void listHelpPage(int i, CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "========= ZavAutoMessager Help =========");
		switch (i) {
		case 1: {
			sender.sendMessage(ChatColor.GOLD + "1. /automessager reload - Reloads config");
			sender.sendMessage(ChatColor.GOLD + "2. /automessager on - Start the messages");
			sender.sendMessage(ChatColor.GOLD + "3. /automessager off - Stops the messages");
			sender.sendMessage(ChatColor.GOLD + "4. /automessager add <message> - Adds a message to the list");
			sender.sendMessage(ChatColor.GOLD + "5. /automessager remove <message number> - Removes message from list");
			sender.sendMessage(ChatColor.GOLD + "=============== Page 1/2 ===============");
			break;
		}
		case 2: {
			sender.sendMessage(ChatColor.GOLD + "6. /automessager ignore - Toggles ignoring messages");
			sender.sendMessage(ChatColor.GOLD + "7. /automessager broadcast <message> - Send a message now");
			sender.sendMessage(ChatColor.GOLD + "8. /automessager about - Displays info about the plugin");
			sender.sendMessage(ChatColor.GOLD + "9. /automessage list - NOT WORKING Shows a message list");
			sender.sendMessage(ChatColor.GOLD + "10. /automessager help (page)- Displays this menu");
			sender.sendMessage(ChatColor.GOLD + "=============== Page 2/2 ===============");
			break;
		}
		}
	}
}
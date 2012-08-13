package com.zavteam.plugins;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

public class Commands implements CommandExecutor {
	private final static String noPerm = ChatColor.RED + "You do not have permission to do this.";
	public Main plugin;
	public Commands(Main instance) {
		plugin = instance;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String freeVariable;
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			if (sender.hasPermission("zavautomessager.view")) {
				if (args.length == 1 || args.length == 0) {
					plugin.MHandler.listHelpPage(1, sender);
				} else {
					try {
						Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "You need to enter a valid page number to do this.");
					}
					if (Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[1]) < 3) {
						plugin.MHandler.listHelpPage(Integer.parseInt(args[1]), sender);
					} else {
						sender.sendMessage(ChatColor.RED + "That is not a valid page number");
					}
				}
			} else {
				sender.sendMessage(noPerm);
			}
		} else if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("zavautomessager.reload")) {
					plugin.messageIt = 0;
					plugin.autoReload();
					plugin.IConfig.loadConfig();
					sender.sendMessage(ChatColor.GREEN + "ZavAutoMessager's config has been reloaded.");
				} else {
					sender.sendMessage(noPerm);
				}
			} else if (args[0].equalsIgnoreCase("on")) {
				if (sender.hasPermission("zavautomessager.toggle")) {
					if (plugin.MConfig.getEnabled()) {
						sender.sendMessage(ChatColor.RED + "Messages are already enabled");
					} else {
						plugin.MConfig.set("enabled", true);
						plugin.MConfig.set("enabled", plugin.MConfig.getEnabled());
						plugin.saveConfig();
						sender.sendMessage(ChatColor.GREEN + "ZavAutoMessager is now on");
					}
				} else {
					sender.sendMessage(noPerm);
				}
			} else if (args[0].equalsIgnoreCase("off")) {
				if (sender.hasPermission("zavautomessager.toggle")) {
					if (!plugin.MConfig.getEnabled()) {
						sender.sendMessage(ChatColor.RED + "Messages are already disabled");
					} else {
						plugin.MConfig.set("enabled", false);
						plugin.MConfig.set("enabled", plugin.MConfig.getEnabled());
						plugin.saveConfig();
						sender.sendMessage(ChatColor.GREEN + "ZavAutoMessager is now off");
					}
				} else {
					sender.sendMessage(noPerm);
				}
			} else if (args[0].equalsIgnoreCase("add")) {
				if (sender.hasPermission("zavautomessager.add")) {
					if (args.length < 2) {
						sender.sendMessage(ChatColor.RED + "You need to enter a chat message to add.");
					} else {
						freeVariable = "";
						for (int i = 1; i < args.length; i++) {
							freeVariable = freeVariable + args[i] + " ";
						}
						freeVariable = freeVariable.trim();
						plugin.messageIt = 0;
						plugin.MHandler.addMessage(freeVariable);
						sender.sendMessage(ChatColor.GREEN + "Your message has been added to the message list.");
					}
				} else {
					sender.sendMessage(noPerm);
				}
			} else if (args[0].equalsIgnoreCase("ignore")) {
				List<String> ignorePlayers = new ArrayList<String>();
				ignorePlayers = plugin.IConfig.getIgnorePlayers();
				if (sender instanceof Player) {
					if (sender.hasPermission("zavautomessager.ignore")) {
						if (ignorePlayers.contains(sender.getName())) {
							ignorePlayers.remove(sender.getName());
							sender.sendMessage(ChatColor.GREEN + "You are no longer ignoring automatic messages");
						} else {
							ignorePlayers.add(sender.getName());
							sender.sendMessage(ChatColor.GREEN + "You are now ignoring automatic messages");
						}
						plugin.IConfig.set("players", ignorePlayers);
						plugin.saveConfig();
					} else {
						sender.sendMessage(noPerm);
					}
				} else {
					plugin.log.info("The console cannot use this command.");
				}
			} else if (args[0].equalsIgnoreCase("broadcast")) {
				String[] cutBroadcastList = new String[10];
				if (sender.hasPermission("zavautomessager.broadcast")) {
					if (args.length < 2) {
						sender.sendMessage(ChatColor.RED + "You must enter a broadcast message");
					} else {
						cutBroadcastList[0] = "";
						for (int i = 1; i < args.length; i++) {
							cutBroadcastList[0] = cutBroadcastList[0] + args[i] + " ";
						}
						cutBroadcastList[0] = cutBroadcastList[0].trim();
						cutBroadcastList[0] = plugin.MConfig.getChatFormat().replace("%msg", cutBroadcastList[0]);
						cutBroadcastList[0] = cutBroadcastList[0].replace("&", "\u00A7");
						cutBroadcastList = ChatPaginator.wordWrap(cutBroadcastList[0], 53);
						plugin.MHandler.handleMessage(cutBroadcastList);
					}
				} else {
					sender.sendMessage(noPerm);
				}
			} else if (args[0].equalsIgnoreCase("about")) {
				if (sender.hasPermission("zavautomessager.about")) {
					sender.sendMessage(ChatColor.GOLD + "You are currently running ZavAutoMessage Version " + plugin.getDescription().getVersion() + ".");
					sender.sendMessage(ChatColor.GOLD + "The latest version is currently version " + plugin.VConfig.getVersion() + ".");
					sender.sendMessage(ChatColor.GOLD + "This plugin was developed by the ZavCodingTeam.");
					sender.sendMessage(ChatColor.GOLD + "Please visit our Bukkit Dev Page for complete details on this plugin.");
				} else {
					sender.sendMessage(noPerm);
				}
			} else if (args[0].equalsIgnoreCase("remove")) {
				if (sender.hasPermission("zavautomessager.remove")) {
					if (args.length < 2) {
						sender.sendMessage(ChatColor.RED + "You need to enter a message number to delete.");
					} else {
						try {
							Integer.parseInt(args[1]);
						} catch (NumberFormatException e) {
							sender.sendMessage(ChatColor.RED + "You have to enter a round number to remove.");
							return false;
						}
						if (Integer.parseInt(args[1]) < 0 || Integer.parseInt(args[1]) > plugin.messages.size() || plugin.messages.size() == 1) {
							sender.sendMessage(ChatColor.RED + "This is not a valid message number");
							sender.sendMessage(ChatColor.RED + "Use /automessager list for a list of messages");
						} else {
							plugin.messages.remove(Integer.parseInt(args[1]) - 1);
							sender.sendMessage(ChatColor.GREEN + "Your message has been removed.");
							plugin.MConfig.set("messages", plugin.messages);
							plugin.messageIt = 0;
							plugin.autoReload();
						}
					}
				} else {
					sender.sendMessage(noPerm);
				}
			} else if (args[0].equalsIgnoreCase("list")) {
				//Command under construction
				sender.sendMessage(ChatColor.RED + "This command is not yet complete sorry :(");
				if (plugin.isEnabled()) {
					return false;
				}
				//
				if (sender.hasPermission("zavautomessager.list")) {
					if (args.length < 1) {
						plugin.MHandler.listPage(1, sender);
						return true;
					}
					try {
						plugin.messages.get(Integer.parseInt(args[1]));
					} catch (Exception e) {
						sender.sendMessage(ChatColor.RED + "You have to enter a valid number to show help page.");
						return false;
					}
					plugin.MHandler.listPage(Integer.parseInt(args[1]), sender);
				} else {
					sender.sendMessage(noPerm);
				}
			} else {
				sender.sendMessage(ChatColor.RED + "ZavAutoMessager did not recognize this command.");
				sender.sendMessage(ChatColor.RED + "Use /automessager help to get a list of commands!");
			}
		}
		return false;
	}
}

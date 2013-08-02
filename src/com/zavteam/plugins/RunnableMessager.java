package com.zavteam.plugins;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;

import com.zavteam.plugins.configs.MainConfig;
import com.zavteam.plugins.messageshandler.MessagesHandler;

public class RunnableMessager implements Runnable {
	
	public ZavAutoMessager plugin;
	
	public RunnableMessager(ZavAutoMessager plugin) {
		this.plugin = plugin;
	}
	
	private int previousMessage;
	
	private static ChatColor[] COLOR_LIST = {ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.DARK_GRAY,
		ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN, ChatColor.LIGHT_PURPLE,
		ChatColor.RED, ChatColor.YELLOW};
	
	@Override
	public void run() {

		boolean messageRandom = MainConfig.getMessageRandom();
		if (MainConfig.getEnabled()) {
			String[] cutMessageList = new String[10];
			if (plugin.messages.size() == 1) {
				plugin.messageIt = 0;
			} else {
				if (messageRandom) {
					plugin.messageIt = getRandomMessage();
				}
			}
			ChatMessage cm = null;
			try {
			cm = plugin.messages.get(plugin.messageIt);
			} catch (Exception e) {
				e.printStackTrace();
				ZavAutoMessager.log.severe("Cannot load messages. There is most likely an error with your config. Please check");
				ZavAutoMessager.log.severe("Shutting down plugin.");
				plugin.disableZavAutoMessager();
			}
			cutMessageList[0] = MainConfig.getChatFormat().replace("%msg", cm.getMessage());
			cutMessageList[0] = cutMessageList[0].replace("&random", getRandomChatColor());
			cutMessageList[0] = ChatColor.translateAlternateColorCodes('&', cutMessageList[0]);
			if (!MainConfig.getChatWrap()) {
				cutMessageList = cutMessageList[0].split("%n");
			} else {
				cutMessageList = ChatPaginator.wordWrap(cutMessageList[0], 59);
			}
			MessagesHandler.handleMessage(cutMessageList, cm);
			if (plugin.messageIt == plugin.messages.size() - 1) {
				plugin.messageIt = 0;
			} else {
				plugin.messageIt = plugin.messageIt + 1;
			}
		}
	}

	private String getRandomChatColor() {
		Random random = new Random();
		return COLOR_LIST[random.nextInt(COLOR_LIST.length)].toString();
	}

	private int getRandomMessage() {
		Random random = new Random();
		if (MainConfig.getForceRandom()) {
			int i = random.nextInt(plugin.messages.size());
			if (!(i == previousMessage)) {
				previousMessage = i;
				return i;
			}
			return getRandomMessage();
		}
		return random.nextInt(plugin.messages.size());
	}

}

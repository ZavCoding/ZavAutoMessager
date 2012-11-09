package com.zavteam.plugins;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;

public class RunnableMessager implements Runnable {
	public Main plugin;
	private int previousMessage;
	public RunnableMessager(Main instance) {
		plugin = instance;
	}

	@Override
	public void run() {

		boolean messageRandom = plugin.MConfig.getMessageRandom();
		if (plugin.MConfig.getEnabled()) {
			String[] cutMessageList = new String[10];
			if (plugin.messages.size() == 1) {
				plugin.messageIt = 0;
			} else {
				if (messageRandom) {
					plugin.messageIt = getRandomMessage();
				}
			}
			cutMessageList[0] = plugin.MConfig.getChatFormat().replace("%msg", plugin.messages.get(plugin.messageIt));
			if (!plugin.MConfig.getChatWrap()) {
				cutMessageList = cutMessageList[0].split("%n");
			} else {
				cutMessageList = ChatPaginator.wordWrap(cutMessageList[0], 59);
			}
			for (int i = 0; i < cutMessageList.length; i++) {
				cutMessageList[i] = ChatColor.translateAlternateColorCodes('&', cutMessageList[i]);
			}
			plugin.MHandler.handleMessage(cutMessageList, plugin.messageIt);
			if (plugin.messageIt == plugin.messages.size() - 1) {
				plugin.messageIt = 0;
			} else {
				plugin.messageIt = plugin.messageIt + 1;
			}
		}
	}

	private int getRandomMessage() {
		Random random = new Random();
		if (plugin.MConfig.getForceRandom()) {
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

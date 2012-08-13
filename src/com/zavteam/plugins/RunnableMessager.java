package com.zavteam.plugins;

import java.util.Random;

import org.bukkit.util.ChatPaginator;

public class RunnableMessager implements Runnable {
	public Main plugin;
	public RunnableMessager(Main instance) {
		plugin = instance;
	}
	
	@Override
	public void run() {
		Random random = new Random();
		
		boolean messageRandom = plugin.MConfig.getMessageRandom();
		if (plugin.MConfig.getEnabled()) {
			String[] cutMessageList = new String[10];
			if (plugin.messages.size() == 1) {
				plugin.messageIt = 0;
			} else {
				if (messageRandom) {
					plugin.messageIt = random.nextInt(plugin.messages.size());
				}
			}
			cutMessageList[0] = plugin.MConfig.getChatFormat().replace("%msg", plugin.messages.get(plugin.messageIt));
			cutMessageList[0] = cutMessageList[0].replace("&", "\u00A7");
			cutMessageList = ChatPaginator.wordWrap(cutMessageList[0], 59);
			plugin.MHandler.handleMessage(cutMessageList);
			if (plugin.messageIt == plugin.messages.size() - 1) {
				plugin.messageIt = 0;
			} else {
				plugin.messageIt = plugin.messageIt + 1;
			}
		}
	}

}

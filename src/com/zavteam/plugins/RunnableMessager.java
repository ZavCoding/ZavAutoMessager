package com.zavteam.plugins;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;

import com.zavteam.plugins.configs.MainConfig;
import com.zavteam.plugins.messageshandler.MessagesHandler;

public class RunnableMessager implements Runnable {
	private int previousMessage;
	@Override
	public void run() {

		boolean messageRandom = MainConfig.getMessageRandom();
		if (MainConfig.getEnabled()) {
			String[] cutMessageList = new String[10];
			if (Main.plugin.messages.size() == 1) {
				Main.plugin.messageIt = 0;
			} else {
				if (messageRandom) {
					Main.plugin.messageIt = getRandomMessage();
				}
			}
			cutMessageList[0] = MainConfig.getChatFormat().replace("%msg", Main.plugin.messages.get(Main.plugin.messageIt));
			cutMessageList[0] = ChatColor.translateAlternateColorCodes('&', cutMessageList[0]);
			if (!MainConfig.getChatWrap()) {
				cutMessageList = cutMessageList[0].split("%n");
			} else {
				cutMessageList = ChatPaginator.wordWrap(cutMessageList[0], 59);
			}
			MessagesHandler.handleMessage(cutMessageList, Main.plugin.messageIt);
			if (Main.plugin.messageIt == Main.plugin.messages.size() - 1) {
				Main.plugin.messageIt = 0;
			} else {
				Main.plugin.messageIt = Main.plugin.messageIt + 1;
			}
		}
	}

	private int getRandomMessage() {
		Random random = new Random();
		if (MainConfig.getForceRandom()) {
			int i = random.nextInt(Main.plugin.messages.size());
			if (!(i == previousMessage)) {
				previousMessage = i;
				return i;
			}
			return getRandomMessage();
		}
		return random.nextInt(Main.plugin.messages.size());
	}

}

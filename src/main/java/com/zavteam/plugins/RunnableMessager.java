package com.zavteam.plugins;

import java.util.Random;

import org.bukkit.ChatColor;

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

		boolean messageRandom = (Boolean) plugin.mainConfig.get("messageinrandomorder", false);
		if ((Boolean) plugin.mainConfig.get("enabled", true)) {
            if (plugin.packets.size() == 1) {
                plugin.messageIterator = 0;
            } else {
                if (messageRandom) {
                    plugin.messageIterator = getRandomMessage();
                }
            }
            AutoPacket autoPacket = plugin.packets.get(plugin.messageIterator);


            if (autoPacket.isCommand()) {
                autoPacket.processAsCommand();
            } else {

            }


			if (plugin.messageIterator == plugin.packets.size() - 1) {
				plugin.messageIterator = 0;
			} else {
				plugin.messageIterator = plugin.messageIterator + 1;
			}
		}
	}

	private String getRandomChatColor() {
		Random random = new Random();
		return COLOR_LIST[random.nextInt(COLOR_LIST.length)].toString();
	}

	private int getRandomMessage() {
		Random random = new Random();
		if ((Boolean) plugin.mainConfig.get("dontrepeatrandommessages", true)) {
			int i = random.nextInt(plugin.packets.size());
			if (!(i == previousMessage)) {
				previousMessage = i;
				return i;
			}
			return getRandomMessage();
		}
		return random.nextInt(plugin.packets.size());
	}

}

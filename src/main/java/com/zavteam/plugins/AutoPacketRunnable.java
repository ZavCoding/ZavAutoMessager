package com.zavteam.plugins;

import com.zavteam.plugins.api.AutoPacketEvent;
import com.zavteam.plugins.api.CommandPacketEvent;
import com.zavteam.plugins.api.MessagePacketEvent;
import com.zavteam.plugins.packets.AutoPacket;
import com.zavteam.plugins.packets.CommandPacket;
import com.zavteam.plugins.packets.MessagePacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Created by Zach on 4/26/14.
 */
public class AutoPacketRunnable implements Runnable {

    private int messageIterator;
    private int previousMessageIndex;
    private ZavAutoMessager zavAutoMessager;

    public AutoPacketRunnable(ZavAutoMessager zavAutoMessager) {
        this.zavAutoMessager = zavAutoMessager;
    }

    @Override
    public void run() {
        if (zavAutoMessager.mainConfig.getConfig().getBoolean("enabled", true)) {
            boolean randomMessaging = zavAutoMessager.mainConfig.getConfig().getBoolean("messageinrandomorder", false);
            boolean permissions = zavAutoMessager.mainConfig.getConfig().getBoolean("permissionsenabled", false);
            if (zavAutoMessager.getAutoPacketList().size() == 1) {
                messageIterator = 0;
            } else if (randomMessaging) {
                messageIterator = getRandomMessage();
            }

            AutoPacket autoPacket = zavAutoMessager.getAutoPacketList().get(messageIterator);
            AutoPacketEvent autoPacketEvent = null;

            if (autoPacket instanceof CommandPacket) {
                CommandPacket commandPacket = (CommandPacket) autoPacket;
                autoPacketEvent = new CommandPacketEvent(commandPacket);
            } else {
                MessagePacket messagePacket = (MessagePacket) autoPacket;
                autoPacketEvent = new MessagePacketEvent(messagePacket);
                MessagePacketEvent messagePacketEvent = (MessagePacketEvent) autoPacketEvent;
                if (permissions && messagePacket.getPermission() != null) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission(messagePacket.getPermission()) && !zavAutoMessager.ignoreConfig.getConfig().getStringList("players").contains(player.getUniqueId().toString())) {
                            messagePacket.getPlayers().add(player.getUniqueId());
                        }
                    }
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!zavAutoMessager.ignoreConfig.getConfig().getStringList("players").contains(player.getUniqueId().toString())) {
                            messagePacket.getPlayers().add(player.getUniqueId());
                        }
                    }
                }
            }

            Bukkit.getPluginManager().callEvent(autoPacketEvent);

            if (autoPacketEvent.isCancelled()) {
                return;
            }

            autoPacket.processPacket();

        }
        return;
    }

    private int getRandomMessage() {
        Random random = new Random();
        if (zavAutoMessager.mainConfig.getConfig().getBoolean("dontrepeatrandommessages", true)) {
            int i = random.nextInt(zavAutoMessager.getAutoPacketList().size());
            if ((i != previousMessageIndex)) {
                previousMessageIndex = i;
                return i;
            }
            return getRandomMessage();
        }
        return random.nextInt(zavAutoMessager.getAutoPacketList().size());
    }

}

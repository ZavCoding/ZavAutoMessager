package com.zavteam.plugins.commands;

import com.zavteam.plugins.ZavAutoMessager;
import com.zavteam.plugins.packets.AutoPacket;
import com.zavteam.plugins.packets.CommandPacket;
import com.zavteam.plugins.packets.MessagePacket;
import com.zavteam.plugins.utils.PluginPM;
import com.zavteam.plugins.utils.PluginPM.MessageType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.CommandHandler;
import se.ranzdo.bukkit.methodcommand.Wildcard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zach on 5/5/14.
 */
public class CommandManager {

    private ZavAutoMessager zavAutoMessager;
    private CommandHandler handler;

    public CommandManager(ZavAutoMessager zavAutoMessager) {
        this.zavAutoMessager = zavAutoMessager;
    }

    public void enableCommands() {
        handler = new CommandHandler(zavAutoMessager);
        handler.registerCommands(this);
    }

    @Command(
            identifier = "automessager",
            description = "ZavAutoMessager Parent Command",
            onlyPlayers = false,
            permissions = {"zavautomessager.view", "zavautomessager.*"}
    )
    public void root(CommandSender sender) {
        zavAutoMessager.getServer().dispatchCommand(sender, "am help 1");
    }

    @Command(
            identifier = "automessager reload",
            description = "Reload the plugins messages",
            onlyPlayers = false,
            permissions = {"zavautomessager.reload", "zavautomessager.*"}
    )
    public void reload(CommandSender sender) {
        zavAutoMessager.reload();
        PluginPM.sendMessage(MessageType.INFO, sender, "The plugin has been reloaded.");
    }

    @Command(
            identifier = "automessager toggle",
            description = "Toggle the plugin on and off",
            onlyPlayers = false,
            permissions = {"zavautomessager.toggle", "zavautomessager.*"}
    )
    public void toggle(CommandSender sender) {
        zavAutoMessager.getMainConfig().getConfig().set("enabled", !zavAutoMessager.getMainConfig().getConfig().getBoolean("enabled"));
        zavAutoMessager.getMainConfig().saveConfig();
        zavAutoMessager.getMainConfig().reloadConfig();
        PluginPM.sendMessage(PluginPM.MessageType.INFO, sender, "Automatic messaging has been set to: " + (zavAutoMessager.getMainConfig().getConfig().getBoolean("enabled") ? "enabled" : "disabled"));
    }

    @Command(
            identifier = "automessager broadcast",
            description = "Broadcast a message using tag formatting",
            onlyPlayers = false,
            permissions = {"zavautomessager.broadcast", "zavautomessager.*"}
    )
    public void broadcast(
            CommandSender sender,
            @Wildcard @Arg(name = "message") String message
    ) {
        PluginPM.sendMessage(PluginPM.MessageType.NO_TAG, zavAutoMessager.getMainConfig().getConfig().getString("chatformat").replace("%msg", message));
    }

    @Command(
            identifier = "automessager list",
            description = "List the messages in the config",
            onlyPlayers = false,
            permissions = {"zavautomessager.list", "zavautomessager.*"}
    )
    public void list(
            CommandSender sender,
            @Arg(name = "page", verifiers = "min[1]", def = "1") int page
    ) {
        try {
            zavAutoMessager.getAutoPacketList().get((5 * page) - 5);
        } catch (IndexOutOfBoundsException e) {
            sender.sendMessage(ChatColor.RED + "You do not have that any messages on that page");
            return;
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "You have to enter an invalid number to show help page.");
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "ZavAutoMessager Messages Page: " + page);
        int initialInt = (5 * page) - 5;
        int finalInt = initialInt + 5;
        for (int iterator = initialInt; iterator < finalInt; iterator++) {
            String message = ChatColor.GOLD + Integer.toString(iterator + 1) + ". ";
            try {
                AutoPacket am = zavAutoMessager.getAutoPacketList().get(iterator);
                if (am instanceof MessagePacket) {
                    MessagePacket mp = (MessagePacket) am;
                    message = message + "Node: " + mp.getPermission() + " Message: " + mp.getMessages().get(0);
                    if (mp.getMessages().size() > 1) {
                        message = message + "...";
                    }
                    message = ChatColor.translateAlternateColorCodes('&', message);
                } else {
                    CommandPacket cp = (CommandPacket) am;
                    message = message + "Command: /" + cp.getCommand();
                }
            } catch (IndexOutOfBoundsException e) {
                message = message + "None";
            }
            sender.sendMessage(message);
        }

    }

    @Command(
            identifier = "automessager ignore",
            description = "Toggle ignoring messages on and off",
            onlyPlayers = true,
            permissions = {"zavautomessager.ignore", "zavautomessager.*"}
    )
    public void ignore(Player sender) {
        List<String> ignorePlayers = new ArrayList<String>();
        ignorePlayers = zavAutoMessager.getIgnoreConfig().getConfig().getStringList("players");
        boolean added = true;
        if (ignorePlayers.contains(sender.getUniqueId().toString())) {
            added = false;
            ignorePlayers.remove(sender.getUniqueId().toString());
        } else {
            ignorePlayers.add(sender.getUniqueId().toString());
        }
        zavAutoMessager.getIgnoreConfig().getConfig().set("players", ignorePlayers);
        zavAutoMessager.getIgnoreConfig().saveConfig();
        zavAutoMessager.getIgnoreConfig().reloadConfig();
        PluginPM.sendMessage(PluginPM.MessageType.INFO, sender, "Ignoring auto messages is: " + (added ? "enabled" : "disabled"));
    }


}

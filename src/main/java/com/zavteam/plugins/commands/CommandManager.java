package com.zavteam.plugins.commands;

import com.zavteam.plugins.ZavAutoMessager;
import com.zavteam.plugins.utils.PluginPM;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.CommandHandler;
import se.ranzdo.bukkit.methodcommand.Wildcard;

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
        help(sender, 1);
    }

    @Command(
            identifier = "automessager help",
            description = "Display the help menu",
            onlyPlayers = false,
            permissions = {"zavautomessager.view", "zavautomessager.*"}
    )
    public void help(
            CommandSender sender,
            @Arg(name = "page", verifiers = "min[1]|max[2]", def = "1") int page
    ) {
        sender.sendMessage(ChatColor.GOLD + "========= ZavAutoMessager Help =========");
        switch (page) {
            case 1: {
                sender.sendMessage(ChatColor.GOLD + "1. /automessager reload - Reloads config");
                sender.sendMessage(ChatColor.GOLD + "2. /automessager toggle - Toggle messaging on and off");
                sender.sendMessage(ChatColor.GOLD + "3. /automessager add <message> - Adds a message to the list");
                sender.sendMessage(ChatColor.GOLD + "4. /automessager remove <message number> - Removes message from list");
                sender.sendMessage(ChatColor.GOLD + "5. /automessager ignore - Toggles ignoring messages");
                sender.sendMessage(ChatColor.GOLD + "=============== Page 1/3 ===============");
                break;
            }
            case 2: {
                sender.sendMessage(ChatColor.GOLD + "6. /automessager broadcast <message> - Send a message now");
                sender.sendMessage(ChatColor.GOLD + "7. /automessager about - Displays info about the Main.plugin");
                sender.sendMessage(ChatColor.GOLD + "8. /automessage list (page) - Shows a message list");
                sender.sendMessage(ChatColor.GOLD + "9. /automessager help (page)- Displays this menu");
                sender.sendMessage(ChatColor.GOLD + "10.");
                sender.sendMessage(ChatColor.GOLD + "=============== Page 2/3 ===============");
                break;
            }
        }
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
            description = "Broadcast",
            onlyPlayers = false,
            permissions = {"zavautomessager.broadcast", "zavautomessager.*"}
    )
    public void broadcast(
            CommandSender sender,
            @Wildcard @Arg(name = "message") String message
    ) {
        PluginPM.sendMessage(PluginPM.MessageType.NO_TAG, zavAutoMessager.getMainConfig().getConfig().getString("chatformat").replace("%msg", message));
    }


}

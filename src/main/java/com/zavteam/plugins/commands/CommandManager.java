package com.zavteam.plugins.commands;

import com.zavteam.plugins.ZavAutoMessager;
import com.zavteam.plugins.utils.PluginPM;
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
    public void help() {

    }

    @Command(
            identifier = "automessager toggle",
            description = "Toggle the plugin on and off",
            onlyPlayers = false,
            permissions = {"zavautomessager.toggle", "zavautomessager.*"}
    )
    public void toggle() {
        zavAutoMessager.getMainConfig().getConfig().set("enabled", false);
        zavAutoMessager.getMainConfig().saveConfig();
        zavAutoMessager.getMainConfig().reloadConfig();
    }

    @Command(
            identifier = "automessager broadcast",
            description = "Broadcast",
            onlyPlayers = false,
            permissions = {"zavautomessager.broadcast", "zavautomessager.*"}
    )
    public void broadcast(
            @Wildcard @Arg(name = "message") String message
    ) {
        PluginPM.sendMessage(PluginPM.MessageType.NO_FORMATTING, zavAutoMessager.getMainConfig().getConfig().getString("chatformat").replace("%msg", message));
    }


}

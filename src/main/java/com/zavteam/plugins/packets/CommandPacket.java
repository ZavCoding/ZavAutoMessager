package com.zavteam.plugins.packets;

import org.bukkit.Bukkit;

/**
 * The CommandPacket is an AutoPacket that is meant to execute a command.
 */
public class CommandPacket extends AutoPacket {

    private String command; // The command to be executed

    public CommandPacket(String command) {
        this.command = command;
    }

    /**
     * Executing the command. Throws exception if the command is empty.
     */
    @Override
    public void processPacket() {

        if (command == null) {
            throw new RuntimeException("A command packet that had no command was attempted to be processed.");
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}

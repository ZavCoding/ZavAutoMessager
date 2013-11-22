package com.zavteam.plugins.autopacket;

import org.bukkit.Bukkit;

/**
 * Created with IntelliJ IDEA.
 * User: suganoz
 * Date: 11/22/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandPacket extends AutoPacket {

    private String command;

    public CommandPacket(String command) {
        this.command = command;
    }

    @Override
    public void processPacket() {
        if (command == null) {
            throw new AutoMessageException("A command packet that had no command was attempted to be processed.");
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

}

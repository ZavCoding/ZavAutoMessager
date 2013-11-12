package com.zavteam.plugins;

import org.bukkit.Bukkit;

public class AutoPacket {

    private String[] messages = new String[10];

    private String permission;

    private boolean isCommand;

    public AutoPacket() {
        this(null);
    }

    public AutoPacket(String[] messages) {
        this(messages, null);
    }

    public AutoPacket(String[] messages, String permission) {
        this(messages, permission, false);
    }

    public AutoPacket(String[] messages, String permission, boolean isCommand) {
        this.messages = messages;
        this.permission = permission;
        this.isCommand = isCommand;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isCommand() {
        return isCommand;
    }

    public void setCommand(boolean command) {
        isCommand = command;
    }

    public void processAsCommand() {
        if (!isCommand) {
            throw new AutoMessageException("Attempting to process a non command packet as a command");
        }
        if (messages[0] == null) {
            throw new AutoMessageException("Attempting to process an auto packet that has no command.");
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), messages[0]);
    }
}

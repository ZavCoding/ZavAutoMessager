package com.zavteam.plugins.packets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Zach on 4/25/14.
 */
public class MessagePacket {

    private String permission; // The permission node associated with this message

    private List<String> messages = new ArrayList<String>();

    public MessagePacket(String message) {
        this(message, null);
    }

    public MessagePacket(String message, String permission) {
        messages.add(message);
        this.permission = permission;
        processMessages();
    }

    public MessagePacket(Collection<String> collection) {
        this(collection, null);
    }

    public MessagePacket(Collection<String> collection, String permission) {
        messages.addAll(collection);
        this.permission = permission;
        processMessages();
    }

    public MessagePacket(String[] messages) {
        this(messages, null);
    }

    public MessagePacket(String[] messages, String permission) {
        for (String message : messages) {
            this.messages.add(message);
        }
        this.permission = permission;
        processMessages();
    }

    /**
     * This method applies all color affects. If the messages variable is only one line in length, it splits the lines up.
     */
    public void processMessages() {

    }

}

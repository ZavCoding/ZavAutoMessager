package com.zavteam.plugins.packets;

import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Zach on 4/25/14.
 */
public class MessagePacket extends AutoPacket {

    private String permission; // The permission node associated with this message

    private List<String> messages = new ArrayList<String>();

    public MessagePacket(String message) {
        this(message, null);
    }

    public MessagePacket(String message, String permission) {
        messages.add(message);
        this.permission = permission;
    }

    public MessagePacket(Collection<String> collection) {
        this(collection, null);
    }

    public MessagePacket(Collection<String> collection, String permission) {
        messages.addAll(collection);
        this.permission = permission;
    }

    public MessagePacket(String[] messages) {
        this(messages, null);
    }

    public MessagePacket(String[] messages, String permission) {
        for (String message : messages) {
            this.messages.add(message);
        }
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * This method applies all color affects. If the messages variable is only one line in length, it splits the lines up.
     */
    public void processMessages(boolean chatPaginating) {

        /**
         * This if section checks if their is only one message in the list.
         * If so it splits the message into the appropriate lines.
         * Their should not be a case where multiple lines are added that still need to be split
         */
        if (messages.size() == 1) {
            List<String> newMessages = new ArrayList<String>();
            newMessages.addAll(Arrays.asList(messages.get(0).split("%n")));
            messages = newMessages;
        }

        /**
         *
         */
        if (chatPaginating) {
            List<String> newMessages = new ArrayList<String>();
            for (String message : messages) {
                newMessages.addAll(Arrays.asList(ChatPaginator.paginate(message, 1).getLines()));
            }
            messages = newMessages;
        }

        /**
         * Color messages
         */
        for (String message : messages) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }



    }

    @Override
    public void processPacket() {

    }
}

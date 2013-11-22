package com.zavteam.plugins.autopacket;

/**
 * Created with IntelliJ IDEA.
 * User: suganoz
 * Date: 11/22/13
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessagePacket extends AutoPacket {

    private String permission;

    private String[] messages = new String[10];

    public MessagePacket(String message) {
        this(message, null);
    }

    public MessagePacket(String message, String permission) {
        messages[0] = message;
        this.permission = permission;
    }

    public MessagePacket(String[] messages) {
        this(messages, null);
    }

    public MessagePacket(String[] messages, String permission) {
        this.messages = messages;
        this.permission = permission;
    }

    @Override
    public void processPacket() {

    }
}

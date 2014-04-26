package com.zavteam.plugins.api;

import com.zavteam.plugins.packets.MessagePacket;

/**
 * Created by Zach on 4/26/14.
 */
public class MessagePacketEvent extends AutoPacketEvent {

    public MessagePacketEvent(MessagePacket messagePacket) {
        super(messagePacket);
    }

}

package com.zavteam.plugins.api;

import com.zavteam.plugins.packets.CommandPacket;

/**
 * Created by Zach on 4/26/14.
 */
public class CommandPacketEvent extends AutoPacketEvent {

    public CommandPacketEvent(CommandPacket commandPacket) {
        super(commandPacket);
    }

    public CommandPacket getAutoPacket() {
        return (CommandPacket) super.getAutoPacket();
    }

}

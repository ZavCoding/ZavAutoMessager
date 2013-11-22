package com.zavteam.plugins.autopacket;

import com.zavteam.plugins.API.AutoPacketEvent;
import org.bukkit.Bukkit;

public abstract class AutoPacket {

    /**
     *
     * Method for processing all packets. This method does an API check.
     * After doing an API check it processes itself.
     *
     */
    public void process() {
        AutoPacketEvent autoPacketEvent = new AutoPacketEvent(this);
        Bukkit.getPluginManager().callEvent(autoPacketEvent);
        if (!autoPacketEvent.isCancelled()) {
            processPacket();
        }

    }

    public abstract void processPacket();

}

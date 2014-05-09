package com.zavteam.plugins.api;

import com.zavteam.plugins.packets.AutoPacket;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AutoPacketEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private AutoPacket autoPacket;

    private boolean cancelled;

    public AutoPacketEvent(AutoPacket autoPacket) {
        this.autoPacket = autoPacket;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void setAutoPacket(AutoPacket autoPacket) {
        this.autoPacket = autoPacket;
    }

    public AutoPacket getAutoPacket() {
        return autoPacket;
    }

    /**
     * @return If the message is cancelled or not
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**

     * Sets whether or not the message is to be cancelled
     */
    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

}
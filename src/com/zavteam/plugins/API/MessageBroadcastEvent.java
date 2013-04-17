package com.zavteam.plugins.API;

import java.util.List;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessageBroadcastEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
    private String[] message;
    
    private boolean cancelled;
    
    private List<String> players;
    
    public MessageBroadcastEvent(String[] message, List<String> players) {
    	this.message = message;
    	this.players = players;
    	this.cancelled = false;
    }
    
    public String[] getMessage() {
        return message;
    }
    
    public List<String> getPlayers() {
    	return players;
    }
    
    public void setMessage(String[] s) {
    	message = s;
    }
    
    public void setPlayers(List<String> s) {
    	players = s;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		cancelled = b;
	}

}

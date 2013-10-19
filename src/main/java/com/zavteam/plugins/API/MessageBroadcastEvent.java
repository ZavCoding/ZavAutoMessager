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
    
    /** Constructor for this event.
     * 
     * @param message The array of messages in the broadcast
     * @param players What players the message is going to be sent to
     */
    public MessageBroadcastEvent(String[] message, List<String> players) {
    	this.message = message;
    	this.players = players;
    	this.cancelled = false;
    }
    
    /**
     * 
     * @return an array of the strings that make up the message that is about to be broadcast
     */
    public String[] getMessage() {
        return message;
    }
    
    /**
     * 
     * @return the players that the message will be sent to
     */
    public List<String> getPlayers() {
    	return players;
    }
    
    /**
     * 
     * @param s the array of messages that you want to be broadcast
     */
    public void setMessage(String[] s) {
    	message = s;
    }
    
    /**
     * 
     * @param s the players that the message will be sent to
     */
    public void setPlayers(List<String> s) {
    	players = s;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    /**
     * 
     * @return If the message is cancelled or not
     * 
     */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/** Sets whether or not the message is to be cancelled
	 * 
	 * 
	 */
	@Override
	public void setCancelled(boolean b) {
		cancelled = b;
	}

}

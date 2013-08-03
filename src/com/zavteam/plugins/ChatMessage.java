package com.zavteam.plugins;

import org.bukkit.Bukkit;

public class ChatMessage {
	
	private String message;
	
	private String permission;
	
	private boolean isCommand;
	
	public ChatMessage(String message, String permission) {
		this.message = message;
		this.permission = permission;
	}
	
	/**
	 * 
	 * @return get the stored message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * 
	 * @param message set the message stored in here
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 
	 * @return get the permission node for this message
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * 
	 * @param permission sets the permission node for this messsage
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	/**
	 * Uses the message as a command instead
	 */
	public void processAsCommand() {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message);
	}

	/**
	 * @return if the message is a command
	 */
	public boolean isCommand() {
		return isCommand;
	}

	/**
	 * @param isCommand Set whether or not this chat message should be executed as a command
	 */
	public void setCommand(boolean isCommand) {
		this.isCommand = isCommand;
	}
	
}

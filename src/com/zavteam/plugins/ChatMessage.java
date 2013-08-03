package com.zavteam.plugins;

public class ChatMessage {
	
	private String message;
	
	private String permission;
	
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
	
}

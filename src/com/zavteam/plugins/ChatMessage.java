package com.zavteam.plugins;

public class ChatMessage {
	
	private String message;
	
	private String permission;
	
	public ChatMessage(String message, String permission) {
		this.message = message;
		this.permission = permission;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
}

package com.zavteam.plugins.API;

import com.zavteam.plugins.messageshandler.MessagesHandler;

public class API {
	
	/** This function is used to immediately broadcast a message using standard formatting for messages
	 * 
	 * @param message The message that will be broadcast
	 */
	public static void broadcastRawMessage(String message) {
		String[] messageArray = new String[1];
		messageArray[0] = message;
		MessagesHandler.handleMessage(messageArray, null);
	}
	
	/** This function is used to immediately broadcast a message(s) using standard formatting for messages
	 * 
	 * @param messages An array of messages that will be broadcast
	 */
	public static void broadcastRawMessage(String[] messages) {
		MessagesHandler.handleMessage(messages, null);
	}
	
	/** This function is used to immediately broadcast a message using standard formatting for messages
	 * 
	 * @param message The message that will be broadcast
	 * @deprecated Named incorrectly. Changed to broadcastRawMessage.
	 */
	@Deprecated
	public static void broadcastMessage(String message) {
		String[] messageArray = new String[1];
		messageArray[0] = message;
		MessagesHandler.handleMessage(messageArray, null);
	}

	/** This function is used to immediately broadcast a message(s) using standard formatting for messages
	 * 
	 * @param messages An array of messages that will be broadcast
	 * @deprecated Named incorrectly. Changed to broadcastRawMessage.
	 */
	@Deprecated
	public static void broadcastMessage(String[] messages) {
		MessagesHandler.handleMessage(messages, null);
	}
	
}

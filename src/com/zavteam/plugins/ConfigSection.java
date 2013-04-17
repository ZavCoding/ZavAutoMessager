package com.zavteam.plugins;

public enum ConfigSection {
	ENABLED,

	WORDWRAP,

	DELAY,

	PERMISSIONSENABLED,

	CHATFORMAT,

	MESSAGEINRANDOMORDER,

	UPDATECHECKING,

	MESSAGESINCONSOLE,

	REQUIREPLAYERSONLINE,

	DONTREPEATRANDOMMESSAGES;
	
	public static boolean contains(String s) {
		for (ConfigSection cs : values()) {
			if (cs.name().equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}
}

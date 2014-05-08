package se.ranzdo.bukkit.methodcommand;

public interface HelpHandler {
	public String[] getHelpMessage(RegisteredCommand command);
	public String getUsage(RegisteredCommand command);
}

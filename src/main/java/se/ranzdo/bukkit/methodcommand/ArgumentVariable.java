package se.ranzdo.bukkit.methodcommand;

import org.bukkit.command.CommandSender;

public interface ArgumentVariable<T> {
	public T var(CommandSender sender, CommandArgument argument, String varName) throws CommandError;
}

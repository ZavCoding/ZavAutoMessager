package se.ranzdo.bukkit.methodcommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import se.ranzdo.bukkit.methodcommand.handlers.*;


public class CommandHandler implements CommandExecutor {
	
	private JavaPlugin plugin;
	private Map<Class<?>, ArgumentHandler<?>> argumentHandlers = new HashMap<Class<?>, ArgumentHandler<?>>();
	private Map<org.bukkit.command.Command, RootCommand> rootCommands = new HashMap<org.bukkit.command.Command, RootCommand>();
	
	private PermissionHandler permissionHandler = new PermissionHandler() {
		@Override
		public boolean hasPermission(CommandSender sender, String[] permissions) {
			for(String perm : permissions) {
				if(!sender.hasPermission(perm))
					return false;
			}
			return true;
		}
	};
	
	private HelpHandler helpHandler = new HelpHandler() {
		private String formatArgument(CommandArgument argument) {
			String def = argument.getDefault();
			if(def.equals(" ")) {
				def = "";
			}
			else if(def.startsWith("?")) {
				String varName = def.substring(1);
				def = argument.getHandler().getVariableUserFriendlyName(varName);
				if(def == null)
					throw new IllegalArgumentException("The ArgumentVariable '"+varName+"' is not registered.");
				def = ChatColor.GOLD+" | "+ChatColor.WHITE+def;
			}
			else {
				def = ChatColor.GOLD+" | "+ChatColor.WHITE+def;
			}
			
			return ChatColor.AQUA+"["+argument.getName()+def+ChatColor.AQUA+"] "+ChatColor.DARK_AQUA+argument.getDescription();
		}
		
		@Override
		public String[] getHelpMessage(RegisteredCommand command) {
			ArrayList<String> message = new ArrayList<String>();
			
			if(command.isSet()) {
				message.add(ChatColor.AQUA+command.getDescription());
			}
			
			message.add(getUsage(command));
			
			if(command.isSet()) {
				for(CommandArgument argument : command.getArguments()) {
					message.add(formatArgument(argument));
				}
				if(command.getWildcard() != null) {
					message.add(formatArgument(command.getWildcard()));
				}
				List<Flag> flags = command.getFlags();
				if(flags.size() > 0) {
					message.add(ChatColor.GOLD+"Flags:");
					for(Flag flag : flags) {
						StringBuilder args = new StringBuilder();
						for(FlagArgument argument : flag.getArguments()) {
							args.append(" ["+argument.getName()+"]");
						}
						message.add("-"+flag.getIdentifier()+ChatColor.AQUA+args.toString());
						for(FlagArgument argument : flag.getArguments()) {
							message.add(formatArgument(argument));
						}
					}
				}
			}
			
			
			List<RegisteredCommand> subcommands = command.getSuffixes();
			if(subcommands.size() > 0) {
				message.add(ChatColor.GOLD+"Subcommands:");
				for(RegisteredCommand scommand : subcommands) {
					message.add(scommand.getUsage());
				}
			}
			
			return message.toArray(new String[0]);
		}
		
		@Override
		public String getUsage(RegisteredCommand command) {
			StringBuilder usage = new StringBuilder();
			usage.append(command.getLabel());
			
			RegisteredCommand parent = command.getParent();
			while(parent != null) {
				usage.insert(0, parent.getLabel()+" ");
				parent = parent.getParent();
			}
			
			usage.insert(0, "/");
			
			if(!command.isSet())
				return usage.toString();
			
			usage.append(ChatColor.AQUA);
			
			for(CommandArgument argument : command.getArguments()) {
				usage.append(" ["+argument.getName()+"]");
			}
			
			usage.append(ChatColor.WHITE);
			
			for(Flag flag : command.getFlags()) {
				usage.append(" (-"+flag.getIdentifier()+ChatColor.AQUA);
				for(FlagArgument arg : flag.getArguments()) {
					usage.append(" ["+arg.getName()+"]");
				}
				usage.append(ChatColor.WHITE+")");
			}
			
			if(command.getWildcard() != null) {
				usage.append(ChatColor.AQUA+" ["+command.getWildcard().getName()+"]");
			}
			
			return usage.toString();
		}
	};
	
	private String helpSuffix = "help";
	
	public CommandHandler(JavaPlugin plugin) {
		this.plugin = plugin;
		
		registerArgumentHandler(String.class, new StringArgumentHandler());
		registerArgumentHandler(int.class, new IntegerArgumentHandler());
		registerArgumentHandler(double.class, new DoubleArgumentHandler());
		registerArgumentHandler(Player.class, new PlayerArgumentHandler());
		registerArgumentHandler(World.class, new WorldArgumentHandler());
		registerArgumentHandler(EntityType.class, new EntityTypeArgumentHandler());
		registerArgumentHandler(Material.class, new MaterialArgumentHandler());
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArgumentHandler<? extends T> getArgumentHandler(Class<T> clazz) {
		return (ArgumentHandler<? extends T>) argumentHandlers.get(clazz);
	}
	
	public HelpHandler getHelpHandler() {
		return helpHandler;
	}
	
	public PermissionHandler getPermissionHandler() {
		return permissionHandler;
	}
	
	public <T> void registerArgumentHandler(Class<? extends T> clazz, ArgumentHandler<T> argHandler) {
		if(argumentHandlers.get(clazz) != null)
			throw new IllegalArgumentException("The is already a ArgumentHandler bound to the class "+clazz.getName()+".");
		
		argHandler.handler = this;
		argumentHandlers.put(clazz, argHandler);
	}
	
	public void registerCommands(Object commands) {
		for(Method method : commands.getClass().getDeclaredMethods()) {
			Command commandAnno = method.getAnnotation(Command.class);
			if(commandAnno == null)
				continue;
			
			String[] identifiers = commandAnno.identifier().split(" ");
			if(identifiers.length == 0) 
				throw new RegisterCommandMethodException(method, "Invalid identifiers");
			
			PluginCommand rootPcommand = plugin.getCommand(identifiers[0]);
			
			if(rootPcommand == null) 
				throw new RegisterCommandMethodException(method, "The rootcommand (the first identifier) is not registerd in the plugin.yml");
			
			if(rootPcommand.getExecutor() != this)
				rootPcommand.setExecutor(this);
			
			RootCommand rootCommand = rootCommands.get(rootPcommand);
			
			if(rootCommand == null) {
				rootCommand = new RootCommand(rootPcommand, this);
				rootCommands.put(rootPcommand, rootCommand);
			}
			
			RegisteredCommand mainCommand = rootCommand;
			
			for(int i = 1; i < identifiers.length; i++) {
				String suffix = identifiers[i];
				if(mainCommand.doesSuffixCommandExist(suffix)) {
					mainCommand = mainCommand.getSuffixCommand(suffix);
				}
				else {
					RegisteredCommand newCommand = new RegisteredCommand(suffix, this, mainCommand);
					mainCommand.addSuffixCommand(suffix, newCommand);
					mainCommand = newCommand;	
				}
			}
			
			mainCommand.set(commands, method);
		}
	}
	
	public void setHelpHandler(HelpHandler helpHandler) {
		this.helpHandler = helpHandler;
	}
	
	public void setPermissionHandler(PermissionHandler permissionHandler) {
		this.permissionHandler = permissionHandler;
	}
	
	public String getHelpSuffix() {
		return helpSuffix;
	}
	
	public void setHelpSuffix(String suffix) {
		this.helpSuffix = suffix;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		RootCommand rootCommand = rootCommands.get(command);
		if(rootCommand == null) {
			return false;
		}
		
		if(rootCommand.onlyPlayers() && !(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED+"Sorry, but only players can execute this command.");
			return true;
		}
		
		rootCommand.execute(sender, args);
		
		return true;
	}
}

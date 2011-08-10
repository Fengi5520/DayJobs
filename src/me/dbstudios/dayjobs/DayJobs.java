package me.dbstudios.dayjobs;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class DayJobs extends JavaPlugin {
	// Assorted public variables
	public Logger log = Logger.getLogger("Minecraft");
	public Boolean debug = false;
	public String defPath = "plugins" + File.separator + "dbstudios" + File.separator + "DayJobs" + File.separator;
	public String prefix = "<DayJobs> ";
	
	// Listeners
	private final DayJobsBlockListener blockListener = new DayJobsBlockListener(this);
	private final DayJobsPlayerListener playerListener = new DayJobsPlayerListener(this);
	
	// Configurations
	public Configuration conf = new Configuration(new File(defPath + "config.yml"));
	public Configuration players = new Configuration(new File(defPath + "players.yml"));
	
	@Override
	public void onDisable() {
		log.info(prefix + "DayJobs is shutting down.");
	}

	@Override
	public void onEnable() {
		/* Initial configuration. Load our debug level as well as register
		 * the needed listener events.
		 */
		conf.load();
		players.load();
		
		if (conf.getString("config.debug") == "true") {
			debug = true;
			log.info(prefix + "Verbose logging enabled.");
		}
		
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
		manager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		// TODO manager.registerEvent(Event.Type.PLAYER_ITEM_HELD, playerListener, Event.Priority.Normal, this);
		ifDebug("Registered block listener and player listener");
		
		log.info(prefix + "DayJobs version 1.0 enabled.");
	}
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("job")) {
			if (args.length == 0) {
				return false;
			} else if (args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage(prefix + "Reloading configuration...");
				
				conf.load();
				players.load();
				
				sender.sendMessage(prefix + "Reload complete.");
				
				return true;
			} else if (args[0].equalsIgnoreCase("list")) {
				sender.sendMessage(prefix + "Coming soon!!!");
			}
		}
		
		return false;
	}
	
	// Public functions used by Block and Player Listeners
	public void ifDebug(String msg) {
		/* The debug handler for DayJobs. Can be called from any function that
		 * Initializes the DayJobs object to output to our debug log.
		 */
		if (debug) {
			log.info(prefix + msg);
		}
	}
	
	public String[] parse_list(String list) {
		/* Strip the starting and ending bracket from a YAML list and parse each comma
		 * separated element into an array, which we return.
		 */
		
		list = list.replace("[", "");
		list = list.replace("]", "");
		return list.split(", ");
	}
	
	public Boolean checkMatch(String player, String block) {
		/* Compares each item in NODES to the placed block stored in BLOCK
		 * If we match, we return true. Otherwise, we return false.
		 */
		
		ifDebug("BlockCheck beginning for player '" + player + "'.");
		
		Boolean matched = false;
		String[] canPlace;
		String tmp = conf.getString("config.all.can-place");
		
		ifDebug("Checking using 'config.all.can-place'");
		
		canPlace = parse_list(tmp);
		
		for (String item : canPlace) {
			ifDebug("Checking item '" + item + "' against block '" + block + "'.");
			
			if (item.equalsIgnoreCase(block)) {
				ifDebug("Match found for '" + item + "'.");
				matched = true;
				break;
			}
		}
		
		if (!matched) {
			String pClass = players.getString("players." + player + ".job");
			
			tmp = conf.getString("config.jobs." + pClass + ".can-place");
			canPlace = parse_list(tmp);
			
			ifDebug("Checking using 'config.jobs." + pClass + ".can-place'.");
			
			for (String item : canPlace) {
				ifDebug("Checking item '" + item + "' against block '" + block + "'.");
				
				if (item.equalsIgnoreCase(block)) {
					ifDebug("Match found for '" + item + "'.");
					matched = true;
					break;
				}
			}
		}
		
		ifDebug("checkMatch returning '" + matched + "'.");
		return matched;
		
		/* Dead code, kept for reference
		Boolean matched = false;
		Integer i = 0;
		
		while (!matched && i < nodes.length) {
			ifDebug("Checking node '" + nodes[i] + "' against block '" + block + "'.");
			if (nodes[i] == block) {
				matched = true;
			}
			
			i++;
		}
		*/
	
		/* Dead code, kept for reference.
		for (String node : nodes) {			
			ifDebug("Checking node '" + node + "' against block '" + block + "'.");
			if (node == block) {
				ifDebug("Match found, returning true.");
				matched = true;
			}
		}
		
		return matched;
		*/
	}
}

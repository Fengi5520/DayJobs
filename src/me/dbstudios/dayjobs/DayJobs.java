package me.dbstudios.dayjobs;

import java.io.File;
import java.util.logging.Logger;

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
		log.info(prefix + " DayJobs is shutting down.");
	}

	@Override
	public void onEnable() {
		/* Initial configuration. Load our debug level as well as register
		 * the needed listener events.
		 */
		conf.load();
		
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
	
	public Boolean checkMatch(String[] nodes, String block) {
		/* Compares each item in NODES to the placed block stored in BLOCK
		 * If we match, we return true. Otherwise, we return false.
		 */
		
		Boolean matched = false;
		Integer i = 0;
		
		while (!matched && i < nodes.length - 1) {
			ifDebug("Checking node '" + nodes[i] + "' against block '" + block + "'.");
			if (nodes[i] == block) {
				matched = true;
			}
			
			i++;
		}
		
		ifDebug("checkMatch returning '" + matched + "'.");
		return matched;
		
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

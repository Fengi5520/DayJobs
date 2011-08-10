package me.dbstudios.dayjobs;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DayJobsBlockListener extends BlockListener {
	private DayJobs bridge;
	
	public DayJobsBlockListener(DayJobs instance) {
		/* Set up our "bridge" between this class and the functions in the
		 * DayJobs class, such as our debugger and our match checker.
		 */
		
		bridge = instance;
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent ev) {
		/* Catch the BlockPlace Event and process the placed block compared to
		 * the list of allowed blocks to determine if the player's class
		 * is permitted to place the chosen block.
		 */
		
		Player player = ev.getPlayer();
		String block = ev.getBlock().getType().toString();
		
		block = block.replace("[", "");
		block = block.replace("]", "");
		
		bridge.ifDebug("Checking block placement for player: '" + player.getDisplayName() + "'");
		
		bridge.conf.load();
		String[] canPlace = bridge.parse_list(bridge.conf.getString("config.all.can-place"));
		
		// Check once if we should cancel the BlockPlaceEvent
		Boolean cancel = bridge.checkMatch(canPlace, block);

		// If our first check failed and CANCEL is still true, check on more time, this time
		// under the player's job class can-place
		if (cancel) {
			String pClass = bridge.players.getString("players." + player.getDisplayName() + ".job");
			canPlace = bridge.parse_list(bridge.conf.getString("config." + pClass + ".can-place"));
			
			if (bridge.checkMatch(canPlace, block)) {
				cancel = false;
				bridge.ifDebug("Check called on " + player.getDisplayName() + " has evaluated TRUE for " + block + ".");
			}
		}
		
		ev.setCancelled(cancel);
	}
}

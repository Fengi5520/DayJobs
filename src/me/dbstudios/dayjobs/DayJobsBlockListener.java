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
		
		// Check once if we should cancel the BlockPlaceEvent. Since checkMatch
		// gives 'true' on a match, we must do the opposite of what checkMatch
		// gives us.
		Boolean cancel = !(bridge.checkMatch(player.getDisplayName(), block));

		// If our first check failed and CANCEL is still true, check on more time, this time
		// under the player's job class can-place
		if (cancel) {
			Boolean cancel = !(bridge.checkMatch(player.getDisplayName(), block));
		}
		
		ev.setCancelled(cancel);
	}
}

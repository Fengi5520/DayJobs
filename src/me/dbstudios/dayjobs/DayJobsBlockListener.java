package me.dbstudios.dayjobs;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DayJobsBlockListener extends BlockListener {
	private DayJobs bridge;
	
	public DayJobsBlockListener(DayJobs instance) {
		bridge = instance;
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent ev) {
		Player player = ev.getPlayer();
		String block = ev.getBlock().getType().toString();
		
		block = block.replace("[", "");
		block = block.replace("]", "");
		
		bridge.ifDebug("Checking block placement for player: '" + player.getDisplayName() + "'");
		
		bridge.conf.load();
		String[] canPlace = bridge.parse_list(bridge.conf.getString("config.all.can-place"));
		
		Boolean cancel = true;
		if (bridge.checkMatch(canPlace, block)) {
			cancel = false;
			bridge.ifDebug("Check called on " + player.getDisplayName() + " has evaluated TRUE for " + block + ".");
		}
		
		if (cancel) {
			if (bridge.checkMatch(canPlace, block)) {
				cancel = false;
				bridge.ifDebug("Check called on " + player.getDisplayName() + " has evaluated TRUE for " + block + ".");
			}
		}
		
		ev.setCancelled(cancel);
	}
}

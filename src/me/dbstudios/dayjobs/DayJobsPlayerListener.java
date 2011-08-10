package me.dbstudios.dayjobs;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class DayJobsPlayerListener extends PlayerListener {
	private DayJobs bridge;
	
	public DayJobsPlayerListener(DayJobs instance) {
		bridge = instance;
	}
	
	/* Temporarily "locking" this event check. Users MUST be manually entered
	 * in players.yml
	public void onPlayerJoin(PlayerJoinEvent ev) {
		bridge.ifDebug("Getting player info from: players." + ev.getPlayer().getDisplayName() + ".job");
		String pClass = bridge.players.getString("players." + ev.getPlayer().getDisplayName() + ".job");
		bridge.ifDebug("Player class is: " + pClass);
		
		if (pClass == "") {
			bridge.players.setProperty("players." + ev.getPlayer().getDisplayName() + ".job", bridge.conf.getString("config.defualt-name"));
			bridge.ifDebug("Creating node players." + ev.getPlayer().getDisplayName() + ".job");
			pClass = "(new) " + bridge.conf.getString("config.default-name");
		}
		
		bridge.ifDebug(ev.getPlayer().getDisplayName() + " has joined as  " + pClass);
	}
	*/
}

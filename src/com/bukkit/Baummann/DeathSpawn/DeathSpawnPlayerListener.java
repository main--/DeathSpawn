package com.bukkit.Baummann.DeathSpawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathSpawnPlayerListener extends PlayerListener {
	public static DeathSpawn plugin;
    
	public DeathSpawnPlayerListener(DeathSpawn instance) {
		plugin = instance;
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		Location loc = new Location(player.getWorld(), DeathSpawn.x, DeathSpawn.y, DeathSpawn.z, DeathSpawn.yaw, DeathSpawn.pitch);
		event.setRespawnLocation(loc);
	}
}

package com.bukkit.Baummann.DeathSpawn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class DeathSpawn extends JavaPlugin {
	private Logger log = Logger.getLogger("Minecraft");
	static String md = "plugins/DeathSpawn";
	static File DS = new File(md + File.separator + "location.dat");
	static Properties props = new Properties();
	static double x;
	static double y;
	static double z;
	static float pitch;
	static float yaw;
	private final DeathSpawnPlayerListener playerListener = new DeathSpawnPlayerListener(this);
	public static PermissionHandler permissionHandler;
    public void onEnable() {
    	log.info("[DeathSpawn] Booting...");
    	new File(md).mkdir();
    	if (!DS.exists()) {
    		try {
    			FileOutputStream out = new FileOutputStream(DS);
    			DS.createNewFile();
    			props.put("x", "0");
    			props.put("y", "64");
    			props.put("z", "0");
    			props.put("pitch", "0");
    			props.put("yaw", "0");
    			props.store(out, "Do NOT edit this config!");
    			out.flush();
    			out.close();
    			loadProcedure();
    		} catch (IOException ex) {
    			ex.printStackTrace();
    		}
    	} else {
    		loadProcedure();
    	}
    	setupPermissions();
    	log.info("[DeathSpawn] Registering events...");
    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Event.Priority.Normal, this);
    	log.info("[DeathSpawn] Done!");
    }
    
    public void onDisable() {
    	log.info("[DeathSpawn] Shutting down...");
    	log.info("[DeathSpawn] Done!");
    }
    
    public void setupPermissions() {
    	Plugin perm = getServer().getPluginManager().getPlugin("Permissions");
    	if (permissionHandler == null) {
    		if (perm != null) {
    			log.info("[DeathSpawn] Permission system detected!");
    			permissionHandler = ((Permissions) perm).getHandler();
    		} else {
    			log.info("[DeathSpawn] Permission system not detected! Shutting down...");
    			getServer().getPluginManager().disablePlugin(this);
    		}
    	}
    }
    
    public void loadProcedure() {
    	try {
    	    FileInputStream in = new FileInputStream(DS);
    	    props.load(in);
    	    x = Double.parseDouble(props.getProperty("x"));
    	    y = Double.parseDouble(props.getProperty("y"));
    	    z = Double.parseDouble(props.getProperty("z"));
    	    pitch = Float.parseFloat(props.getProperty("pitch"));
    	    yaw = Float.parseFloat(props.getProperty("yaw"));
    	    in.close();
    	} catch (IOException ex) {
    	    ex.printStackTrace();	
        }
    }
    
    public void setDeathSpawnTo(String sx, String sy, String sz, String spitch, String syaw) {
    	try {
    	   FileOutputStream out = new FileOutputStream(DS);
           props.setProperty("x", sx);
           props.setProperty("y", sy);
           props.setProperty("z", sz);
           props.setProperty("yaw", syaw);
           props.setProperty("pitch", spitch);
           props.store(out, "Do NOT edit this config!");
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLine, String[] split) {
    	if (cmd.getName().equalsIgnoreCase("setdeathspawn")) {
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
    			if (permissionHandler.has(player, "deathspawn.setspawn")) {
    				String sx = String.valueOf(player.getLocation().getX());
    				String sy = String.valueOf(player.getLocation().getY());
    				String sz = String.valueOf(player.getLocation().getZ());
    				String syaw = String.valueOf(player.getLocation().getYaw());
    				String spitch = String.valueOf(player.getLocation().getPitch());
    				setDeathSpawnTo(sx, sy, sz, spitch, syaw);
    				loadProcedure();
    				player.sendMessage(ChatColor.GREEN + "Successfully changed the DeathSpawn location!");
    				log.info("[DeathSpawn] The DeathSpawn location has been changed by " + player.getDisplayName());
    				return true;
    			} else {
    				player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
    				return true;
    			}
    		} else {
    			log.warning("You are not a valid entity!");
    			return true;
    		}
    	}
    	return false;
    }
}

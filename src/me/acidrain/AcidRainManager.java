package me.acidrain;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class AcidRainManager extends BukkitRunnable {
	private final World world;
	private final Main plugin;
	
	public AcidRainManager(World world, Main plugin) {
		this.world = world;
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().size() > 0) {
			boolean isEnabled = plugin.getConfig().getBoolean("acidrain", true);
			
			if (isEnabled && world.hasStorm()) {
				world.getPlayers().forEach(this::damage);
			}
		}
	}
	
	private void damage(Player player) {
		if (isOutside(player) && !isUnderCover(player)) {
			ItemStack helmet = player.getEquipment().getHelmet();
			String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
			Material allowedHelmet = version.compareTo("v1_16_R1") >= 0 && version.compareTo("v1_19_R4") <= 0 ? Material.NETHERITE_HELMET : Material.DIAMOND_HELMET;
			
			if (helmet == null || helmet.getType() != allowedHelmet) {
				player.damage(1.0);
			}
		}
	}
	
	private boolean isOutside(Player player) {
		return player.getLocation().getBlock().getLightFromSky() == 15;
	}
	
	private boolean isUnderCover(Player player) {
		return player.getLocation().getBlock().getLightFromBlocks() >= 8;
	}
}
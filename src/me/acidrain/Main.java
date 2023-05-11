package me.acidrain;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.io.File;

public class Main extends JavaPlugin implements CommandExecutor {
	private AcidRainManager acidRainManager;
	
	@Override
	public void onEnable() {
		String version = getServer().getClass().getPackage().getName().split("\\.")[3];
		
		if (version.compareTo("v1_16_R1") >= 0 && version.compareTo("v1_19_R4") <= 0) {
			getServer().getConsoleSender().sendMessage("§e[!] §aYOUR VERSION " + Bukkit.getVersion() + " YOU WILL USE NETHERITE_HELMET");
		}
		else {
			getServer().getConsoleSender().sendMessage("§e[!] §aYOUR VERSION " + Bukkit.getVersion() + " YOU WILL USE DIAMOND_HELMET");
		}
		
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		
		File configFile = new File(getDataFolder(), "config.yml");
		
		if (!configFile.exists()) {
			saveResource("config.yml", false);
		}
		
		loadConfig();
		
		acidRainManager = new AcidRainManager(getServer().getWorld("world"), this);
		acidRainManager.runTaskTimer(this, 0L, 20L); //1 SEC
	}
	
	@Override
	public void onDisable() {
		saveConfig();
		
		if (acidRainManager != null) {
			acidRainManager.cancel();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if (!player.hasPermission("acidrain.access")) {
				player.sendMessage("§cYou do not have permission to use this command.");
				return true;
			}
		}
		
		if (args.length == 0) {
			sender.sendMessage("§fCommand usage:");
			sender.sendMessage("§c/acidrain reload - §freload configuration");
			sender.sendMessage("§c/acidrain change [true/false] - §fchange acid rain state");
			return true;
		}
		
		String reload = args[0].toLowerCase();
		
		switch (reload) {
			case "reload": {
				reloadConfig();
				loadConfig();
				
				sender.sendMessage("§e[!] §aThe configuration of the §cAcidRain §aplugin has been reloaded.");
				return true;
			}
			
			case "change": {
				if (args.length < 2) {
					sender.sendMessage("§fCommand usage:");
					sender.sendMessage("§c/acidrain reload - §freload configuration");
					sender.sendMessage("§c/acidrain change [true/false] - §fchange acid rain state");
					return true;
				}
				
				String value = args[1].toLowerCase();
				
				switch (value) {
					case "true": {
						getConfig().set("acidrain", true);
						saveConfig();
						
						sender.sendMessage("§e[!] §aAcid rain enabled.");
						return true;
					}
					
					case "false": {
						getConfig().set("acidrain", false);
						saveConfig();
						
						sender.sendMessage("§e[!] §cAcid rain disabled.");
						return true;
					}
					
					default: {
						sender.sendMessage("§fCommand usage:");
						sender.sendMessage("§c/acidrain reload - §freload configuration");
						sender.sendMessage("§c/acidrain change [true/false] - §fchange acid rain state");
						return true;
					}
				}
			}
			
			default: {
				sender.sendMessage("§fCommand usage:");
				sender.sendMessage("§c/acidrain reload - §freload configuration");
				sender.sendMessage("§c/acidrain change [true/false] - §fchange acid rain state");
				return true;
			}
		}
	}
	
	private void loadConfig() {
		if (!getConfig().contains("acidrain")) {
			getConfig().addDefault("acidrain", true);
			getConfig().options().copyDefaults(true);
			getConfig().options().copyHeader(true);
			saveConfig();
		}
	}
}
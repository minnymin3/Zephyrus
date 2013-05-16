package minny.zephyrus.player;

import minny.zephyrus.Zephyrus;
import minny.zephyrus.utils.PlayerConfigHandler;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class LevelManager {

	PlayerConfigHandler config;
	public Zephyrus plugin;
	private int levelBalance;
	
	public LevelManager(Zephyrus plugin) {
		this.plugin = plugin;
	}

	public void levelUp(Player player) {
		config = new PlayerConfigHandler(plugin, player.getName());
		config.reloadConfig();
		int current = config.getConfig().getInt("Level");
		current = current + 1;
		config.getConfig().set("Level", current);
		config.saveConfig();
		config.reloadConfig();
		plugin.mana.put(player.getName(), current * 100);
		player.sendMessage(ChatColor.AQUA + "You leveled up to level "
				+ getLevel(player));
		player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2, 1);
		player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2, 8);
		player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2, -1);
	}

	public void levelProgress(Player player, int amount) {
		this.levelBalance = plugin.getConfig().getInt("LevelBalance");
		config = new PlayerConfigHandler(plugin, player.getName());
		config.reloadConfig();
		int current = config.getConfig().getInt("progress");
		current = current + amount;
		if (current > getLevel(player) * levelBalance - 12) {
			current = current - (getLevel(player) * levelBalance - 12);
			levelUp(player);
		}
		config.getConfig().set("progress", current);
		config.saveConfig();
		config.reloadConfig();
	}

	public int getLevelProgress(Player player) {
		config = new PlayerConfigHandler(plugin, player.getName());
		config.reloadConfig();
		int current = config.getConfig().getInt("progress");
		return current;
	}

	public int getLevel(Player player) {
		config = new PlayerConfigHandler(plugin, player.getName());
		config.reloadConfig();
		return config.getConfig().getInt("Level");
	}

	public int getLevel(HumanEntity player) {
		config = new PlayerConfigHandler(plugin, player.getName());
		config.reloadConfig();
		return config.getConfig().getInt("Level");
	}

	public void resetMana(Player player) {
		plugin.mana.put(player.getName(), getLevel(player) * 100);
	}

	public int getMana(Player player) {
		try {
			return (Integer) plugin.mana.get(player.getName());
		} catch (Exception e) {
			resetMana(player);
			return getLevel(player) * 100;
		}
	}

	public void saveMana(Player player) {
		config = new PlayerConfigHandler(plugin, player.getName());
		config.getConfig().set("mana", plugin.mana.get(player.getName()));
		config.saveConfig();
		config.reloadConfig();
	}

	public int loadMana(Player player) {
		config = new PlayerConfigHandler(plugin, player.getName());
		config.reloadConfig();
		int i = config.getConfig().getInt("mana");
		return i;
	}

	public void drainMana(Player player, int amount) {
		plugin.mana.put(player.getName(),
				(Integer) plugin.mana.get(player.getName()) - amount);
	}

	public void displayMana(Player player) {
		double maxMana = getLevel(player) * 100;
		double currentMana = getMana(player);
		double devider = maxMana / 120;
		StringBuffer full = new StringBuffer();
		StringBuffer empty = new StringBuffer();
		if (currentMana != 0) {
			double emptyMana = maxMana - currentMana;
			for (double i = currentMana / devider; i >= 0; i = i - 1) {
				full.append("|");
			}
			for (double i = emptyMana / devider; i >= 1; i = i - 1) {
				if (emptyMana >= 1) {
					empty.append("|");
				} else {
					break;
				}
			}
			player.sendMessage(ChatColor.GOLD + "        ---===["
					+ ChatColor.RED + "Mana: " + currentMana + " / " + maxMana
					+ ChatColor.GOLD + "]===---");
			player.sendMessage(ChatColor.DARK_AQUA + "{" + ChatColor.AQUA
					+ full + ChatColor.GRAY + empty + ChatColor.DARK_AQUA + "}");
			player.sendMessage(ChatColor.DARK_AQUA + "{" + ChatColor.AQUA
					+ full + ChatColor.GRAY + empty + ChatColor.DARK_AQUA + "}");
		} else {
			for (int i = 120; i > 0; i = i - 1) {
				empty.append("|");
			}
			player.sendMessage(ChatColor.GOLD + "              ---===["
					+ ChatColor.RED + "Mana: " + currentMana + " / " + maxMana
					+ ChatColor.GOLD + "]===---");
			player.sendMessage(ChatColor.DARK_AQUA + "{" + ChatColor.GRAY
					+ empty + ChatColor.DARK_AQUA + "}");
			player.sendMessage(ChatColor.DARK_AQUA + "{" + ChatColor.GRAY
					+ empty + ChatColor.DARK_AQUA + "}");
		}

	}

	public void displayLevel(Player player) {
		this.levelBalance = plugin.getConfig().getInt("LevelBalance");
		int level = getLevel(player);
		int currentLevelProg = getLevelProgress(player);
		int maxLevelProg = level * levelBalance - 12;
		double devider = maxLevelProg / 120;
		StringBuffer full = new StringBuffer();
		StringBuffer empty = new StringBuffer();
		if (currentLevelProg != 0) {
			double emptyLvl = maxLevelProg - currentLevelProg;
			for (double i = currentLevelProg / devider; i >= 0; i = i - 1) {
				full.append("|");
			}
			for (double i = emptyLvl / devider; i >= 1; i = i - 1) {
				if (emptyLvl >= 1) {
					empty.append("|");
				} else {
					break;
				}
			}
			player.sendMessage(ChatColor.DARK_BLUE + " ---===["
					+ ChatColor.BLUE + "Level: " + level + ChatColor.BOLD + ""
					+ ChatColor.DARK_BLUE + " -=- " + ChatColor.BLUE
					+ "Progress: " + currentLevelProg + "/" + maxLevelProg
					+ ChatColor.DARK_BLUE + "]===---");
			player.sendMessage(ChatColor.DARK_GRAY + "{"
					+ ChatColor.LIGHT_PURPLE + full + ChatColor.GRAY + empty
					+ ChatColor.DARK_GRAY + "}");
		} else {
			for (int i = 120; i > 0; i = i - 1) {
				empty.append("|");
			}
			player.sendMessage(ChatColor.DARK_BLUE + " ---===["
					+ ChatColor.BLUE + "Level: " + level + ChatColor.DARK_BLUE
					+ " -=- " + ChatColor.DARK_BLUE + "Progress: "
					+ currentLevelProg + "/" + maxLevelProg
					+ ChatColor.DARK_BLUE + "]===---");
			player.sendMessage(ChatColor.DARK_AQUA + "{" + ChatColor.GRAY
					+ empty + ChatColor.DARK_AQUA + "}");
		}

	}

}

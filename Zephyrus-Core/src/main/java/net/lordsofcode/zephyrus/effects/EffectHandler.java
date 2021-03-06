package net.lordsofcode.zephyrus.effects;

import java.util.HashMap;
import java.util.Map;

import net.lordsofcode.zephyrus.Zephyrus;
import net.lordsofcode.zephyrus.api.IUser;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Zephyrus
 * 
 * @author minnymin3
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */

public class EffectHandler implements Listener {

	private Map<String, BukkitTask> runnableMap;

	public EffectHandler() {
		runnableMap = new HashMap<String, BukkitTask>();
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Zephyrus.getEffectMap().put(event.getPlayer().getName(), new HashMap<Integer, Integer>());
		for (EffectType effect : EffectType.values()) {
			effect.getEffect().onStartup(event.getPlayer());
		}
		runnableMap.put(event.getPlayer().getName(),
				new EffectTimer(event.getPlayer().getName()).runTaskTimer(Zephyrus.getPlugin(), 1, 1));
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		Zephyrus.getEffectMap().remove(event.getPlayer().getName());
		if (runnableMap.containsKey(event.getPlayer().getName()) && runnableMap.get(event.getPlayer().getName()) != null) {
			runnableMap.get(event.getPlayer().getName()).cancel();
			runnableMap.remove(event.getPlayer().getName());
		}
	}
	
	public static boolean hasEffect(Player player, EffectType type) {
		return hasEffect(player.getName(), type);
	}
	
	public static boolean hasEffect(String playerName, EffectType type) {
		if (Zephyrus.getEffectMap().containsKey(playerName)) {
			return Zephyrus.getEffectMap().get(playerName).containsKey(type.getID());
		}
		return false;
	}
	
	private class EffectTimer extends BukkitRunnable {

		private String playerName;
		private int tickTime;
		
		public EffectTimer(String playerName) {
			this.playerName = playerName;
			this.tickTime = 0;
		}
		
		@Override
		public void run() {
			Player player = Bukkit.getPlayer(playerName);
			if (player != null) {
				IUser user = Zephyrus.getUser(player);
				for (EffectType type : user.getCurrentEffects()) {
					int time = type.getEffect().getTickTime();
					float tick = tickTime/(float)time;
					if (time != 0 && tick == (int) tick) {
						type.getEffect().onTick(player);
					}
					int remaining = user.getEffectTime(type)-1;
					Zephyrus.getEffectMap().get(player.getName()).put(type.getID(), remaining);
					if (remaining <= 0) {
						user.removeEffect(type);
					}
				}
			}
			if (tickTime == 20) {
				tickTime = 0;
			}
			tickTime++;
		}

	}

}

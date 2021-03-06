package net.lordsofcode.zephyrus.events;

import java.util.List;

import net.lordsofcode.zephyrus.api.ICustomItem;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

/**
 * Zephyrus
 * 
 * @author minnymin3
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */

public class PlayerCraftCustomItemEvent extends Event implements Cancellable {

	private boolean cancelled;
	private List<HumanEntity> player;
	private ICustomItem item;
	private PrepareItemCraftEvent e;

	public PlayerCraftCustomItemEvent(List<HumanEntity> player, ICustomItem item, PrepareItemCraftEvent e) {
		this.player = player;
		this.item = item;
		this.e = e;
	}

	/**
	 * The humans crafting the item
	 * 
	 * @return
	 */
	public List<HumanEntity> getPlayers() {
		return player;
	}

	/**
	 * Returns the crafter of the item
	 * 
	 * @return Null if the crafter is not a player
	 */
	public Player getPlayer() {
		if (player.get(0) instanceof Player) {
			return (Player) player.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Gets the event of crafting the item
	 * 
	 * @return
	 */
	public PrepareItemCraftEvent getPrepareItemCraft() {
		return e;
	}

	/**
	 * Gets the item being crafted
	 * 
	 * @return
	 */
	public ICustomItem getItem() {
		return item;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}

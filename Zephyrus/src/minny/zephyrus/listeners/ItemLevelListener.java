package minny.zephyrus.listeners;

import minny.zephyrus.Zephyrus;
import minny.zephyrus.items.CustomItem;
import minny.zephyrus.utils.merchant.Merchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemLevelListener implements Listener {

	Zephyrus plugin;

	public ItemLevelListener(Zephyrus plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onClickWithItem(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = e.getClickedBlock();
			byte b = 12;
			if (block.getType() == Material.ENCHANTMENT_TABLE
					&& block.getData() == b) {
				ItemStack i = e.getItem();
				if (i != null
						&& i.hasItemMeta()
						&& i.getItemMeta().hasDisplayName()
						&& Zephyrus.itemMap.containsKey(i.getItemMeta()
								.getDisplayName())) {
					e.setCancelled(true);
					try {
						new CraftLivingEntity(null, null);
					} catch (NoClassDefFoundError err) {
						e.getPlayer()
								.sendMessage(
										"Saadly, the version of CraftBukkit running on this server is not fully compatible with this version of Zephyrus. This feature has been disabled...");
						return;
					}
					CustomItem customItem = Zephyrus.itemMap.get(i
							.getItemMeta().getDisplayName());
					if (!(customItem.getItemLevel(i) < customItem.maxLevel())) {
						e.getPlayer().sendMessage(
								"That item cannot be leveled anymore!");
						return;
					}
					if (Zephyrus.merchantMap.containsKey(e.getItem())) {
						Merchant m = Zephyrus.merchantMap.get(e.getItem());
						m.openTrading(e.getPlayer());
						plugin.invPlayers.put(e.getPlayer().getName(), m);
					} else {
						e.getPlayer().sendMessage("Something went wrong. Item not found...");
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (plugin.invPlayers.containsKey(e.getPlayer().getName())) {
			plugin.invPlayers.remove(e.getPlayer().getName());
		}
	}

	@EventHandler
	public void onClickInv(InventoryClickEvent e) {
		if (plugin.invPlayers.containsKey(e.getWhoClicked().getName())) {
			if (e.getInventory().getType() == InventoryType.MERCHANT) {
				Merchant m = plugin.invPlayers.get(e.getWhoClicked().getName());
				ItemStack i = e.getCurrentItem();
				ItemStack mi = m.getOffers().get(0).getFirstInput();
				ItemStack i2 = e.getCursor();
				if (e.getRawSlot() != 0 && e.getRawSlot() != 1
						&& e.getRawSlot() != 2 && i2 != null && i != null
						&& !i.equals(mi)
						&& !i2.equals(mi)
						&& i.getType() != Material.EMERALD
						&& i2.getType() != Material.EMERALD) {
					e.setCancelled(true);
				}
			}
		}
	}
}

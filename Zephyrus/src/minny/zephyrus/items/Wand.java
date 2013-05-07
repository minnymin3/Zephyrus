package minny.zephyrus.items;

import java.util.HashSet;

import minny.zephyrus.Zephyrus;
import minny.zephyrus.spells.Spell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class Wand extends CustomItem {

	public Wand(Zephyrus plugin) {
		super(plugin);
		plugin.getServer().addRecipe(recipe());
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public String name() {
		return "�6Wand";
	}

	@Override
	public ItemStack item() {
		ItemStack i = new ItemStack(Material.STICK);
		createItem(i);
		return i;
	}

	@Override
	public void createItem(ItemStack i) {
		setItemName(i, this.name());
		setGlow(i);
	}

	@Override
	public Recipe recipe() {
		ShapedRecipe recipe = new ShapedRecipe(item());
		recipe.shape("  C", " B ", "A  ");
		recipe.setIngredient('C', Material.GLOWSTONE_DUST);
		recipe.setIngredient('B', Material.STICK);
		recipe.setIngredient('A', Material.GOLD_INGOT);
		return recipe;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getClickedBlock().getType() == Material.BOOKSHELF) {
			Location loc = e.getClickedBlock().getLocation();
			loc.setY(loc.getY() + 1);
			Entity[] entitys = getNearbyEntities(loc, 1);
			if (getItem(entitys) != null) {
				ItemStack i = getItem(entitys);
				if (plugin.spellCraftMap.containsKey(i)) {
					Spell s = plugin.spellCraftMap.get(i);
					if (!(s.getLevel(e.getPlayer()) < s.reqLevel())) {
						getItemEntity(entitys).remove();
						s.dropSpell(e.getClickedBlock(), s.name());
					} else {
						e.getPlayer().sendMessage(
								"This spell requires level " + s.reqLevel());
						;
					}
				} else {
					e.getPlayer().sendMessage("Spell recipe not found");
				}
			} else {
				e.getPlayer().sendMessage("Spell recipe not found");
			}
		}
	}

	public static Entity[] getNearbyEntities(Location l, int radius) {
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
		HashSet<Entity> radiusEntities = new HashSet<Entity>();
		for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
			for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
				int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
				for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z
						+ (chZ * 16)).getChunk().getEntities()) {
					if (e.getLocation().distance(l) <= radius
							&& e.getLocation().getBlock() != l.getBlock())
						radiusEntities.add(e);
				}
			}
		}
		return radiusEntities.toArray(new Entity[radiusEntities.size()]);
	}

	public static ItemStack getItem(Entity[] entitys) {
		for (Entity e : entitys) {
			if (e.getType() == EntityType.DROPPED_ITEM) {
				Item i = (Item) e;
				return i.getItemStack();
			}
		}
		return null;
	}
	
	public static Item getItemEntity(Entity[] entitys) {
		for (Entity e : entitys) {
			if (e.getType() == EntityType.DROPPED_ITEM) {
				Item i = (Item) e;
				return i;
			}
		}
		return null;
	}

}

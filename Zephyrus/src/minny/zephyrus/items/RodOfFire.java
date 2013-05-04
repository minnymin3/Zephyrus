package minny.zephyrus.items;

import minny.zephyrus.LevelManager;
import minny.zephyrus.Zephyrus;
import minny.zephyrus.utils.RecipeUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class RodOfFire extends Item {

	RecipeUtil recipe;
	LevelManager lvl;
	
	public RodOfFire(Zephyrus plugin) {
		super(plugin);
		recipe = new RecipeUtil();
		lvl = new LevelManager(plugin);
		plugin.getServer().addRecipe(recipe());
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public String name() {
		return "�cRod of Fire";
	}

	@Override
	public void createItem(ItemStack i) {
		setItemName(i, "Rod of Fire", "c");
		setItemLevel(i, 1);
		setGlow(i);
	}

	@Override
	public Recipe recipe() {
		ItemStack fire_rod = new ItemStack(Material.BLAZE_ROD);
		createItem(fire_rod);

		ShapedRecipe recipe = new ShapedRecipe(fire_rod);
		recipe.shape("BCB", "BAB", "BBB");
		recipe.setIngredient('C', Material.DIAMOND);
		recipe.setIngredient('B', Material.BLAZE_POWDER);
		recipe.setIngredient('A', Material.BLAZE_ROD);
		return recipe;
	}

	@Override
	public ItemStack item() {
		ItemStack i = new ItemStack(Material.BLAZE_ROD);
		createItem(i);
		return i;
	}

	@Override
	public int reqLevel() {
		return 1;
	}
	
	@EventHandler
	public void fireball(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR
				&& checkName(e.getPlayer().getItemInHand(), "�cRod of Fire")
				&& !plugin.fireRodDelay.containsKey(e.getPlayer().getName())
				&& getItemLevel(e.getPlayer().getItemInHand()) < 6) {
			Player player = e.getPlayer();
			Fireball fireball = player.launchProjectile(SmallFireball.class);
			fireball.setVelocity(fireball.getVelocity().multiply(10));
			delay(plugin.fireRodDelay, plugin,
					delayFromLevel(getItemLevel(player.getItemInHand())), e
							.getPlayer().getName());
		} else if (e.getAction() == Action.RIGHT_CLICK_AIR
				&& checkName(e.getPlayer().getItemInHand(), "�cRod of Fire")
				&& !plugin.fireRodDelay.containsKey(e.getPlayer().getName())) {
			Player player = e.getPlayer();
			Fireball fireball = player.launchProjectile(Fireball.class);
			fireball.setVelocity(fireball.getVelocity().multiply(10));
			delay(plugin.fireRodDelay, plugin,
					delayFromLevel(getItemLevel(player.getItemInHand())), e
							.getPlayer().getName());
		} else if (e.getAction() == Action.RIGHT_CLICK_AIR
				&& plugin.fireRodDelay.containsKey(e.getPlayer().getName())
				&& checkName(e.getPlayer().getItemInHand(), "�cRod of Fire")) {
			int time = (Integer) plugin.fireRodDelay.get(e.getPlayer().getName());
			e.getPlayer().sendMessage(
					ChatColor.GRAY + "The rod of fire still needs " + time
							+ " seconds to recharge!");
		}
	}
	
	@EventHandler
	public void onCraft(PrepareItemCraftEvent e){
		RodOfFire fire = new RodOfFire(plugin);
		Recipe r = fire.recipe();
		if (RecipeUtil.areEqual(e.getRecipe(), r)){
			HumanEntity p = e.getViewers().get(0);
			if (lvl.getLevel(p) < reqLevel()){
				p.getServer().broadcastMessage(String.valueOf(lvl.getLevel(p)));
				e.getInventory().setResult(null);
			}
		}
	}
}

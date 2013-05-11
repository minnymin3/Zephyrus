package minny.zephyrus.spells;

import java.util.HashSet;
import java.util.Set;

import minny.zephyrus.Zephyrus;
import minny.zephyrus.hooks.PluginHook;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Bolt extends Spell {

	PluginHook hook;
	
	public Bolt(Zephyrus plugin) {
		super(plugin);
		this.hook = new PluginHook();	
	}

	@Override
	public String name() {
		return "bolt";
	}
	
	@Override
	public String bookText() {
		return "Strikes lightning where you point!";
	}

	@Override
	public int reqLevel() {
		return 0;
	}

	@Override
	public int manaCost() {
		return 20;
	}

	@Override
	public void run(Player player) {
		Location loc = player.getTargetBlock(null, 1000).getLocation();
		player.getWorld().strikeLightning(loc);
	}

	@Override
	public Set<ItemStack> spellItems() {
		Set<ItemStack> items = new HashSet<ItemStack>();
		items.add(new ItemStack(Material.EMERALD));
		items.add(new ItemStack(Material.FLINT_AND_STEEL));
		return items;
	}
	
	@Override
	public boolean canRun(Player player) {
		if (hook.worldGuard()) {
			hook.wgHook();
			if (hook.wg.canBuild(player, player.getTargetBlock(null, 1000))){
				player.sendMessage("Can Build");
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String failMessage(){
		return ChatColor.DARK_RED + "You don't have permission for this area";
	}

}

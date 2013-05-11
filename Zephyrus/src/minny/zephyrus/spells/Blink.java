package minny.zephyrus.spells;

import java.util.HashSet;
import java.util.Set;

import minny.zephyrus.Zephyrus;
import minny.zephyrus.hooks.PluginHook;
import minny.zephyrus.utils.ParticleEffects;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Blink extends Spell {

	public Blink(Zephyrus plugin) {
		super(plugin);
	}

	@Override
	public String name() {
		return "blink";
	}

	@Override
	public String bookText() {
		return "Gets you from point A to point C without bothering with point B";
	}
	
	@Override
	public int reqLevel() {
		return 2;
	}

	@Override
	public int manaCost() {
		return 8;
	}

	@Override
	public Set<ItemStack> spellItems(){
		Set<ItemStack> i = new HashSet<ItemStack>();
		i.add(new ItemStack(Material.ENDER_PEARL));
		return i;
	}
	
	@Override
	public void run(Player player) {
		Location loc = player.getTargetBlock(null, 100).getLocation();
		loc.setY(loc.getY() + 1);
		loc.setPitch(player.getLocation().getPitch());
		loc.setYaw(player.getLocation().getYaw());
		try {
			ParticleEffects.sendToLocation(ParticleEffects.TOWN_AURA, loc, 1,
					1, 1, 1, 10);
			ParticleEffects.sendToLocation(ParticleEffects.PORTAL,
					player.getLocation(), 1, 1, 1, 1, 16);
		} catch (Exception e) {
		}
		player.getWorld().playSound(player.getLocation(),
				Sound.ENDERMAN_TELEPORT, 10, 1);
		player.teleport(loc);
	}

	@Override
	public boolean canRun(Player player) {
		if (player.getTargetBlock(null, 100) != null
				&& player.getTargetBlock(null, 100).getType() != Material.AIR) {
			PluginHook hook = new PluginHook();
			Location loc = player.getTargetBlock(null, 100).getLocation();
			loc.setY(loc.getY() + 1);
			Location loc2 = loc;
			loc2.setY(loc2.getY() + 1);
			Block block = loc.getBlock();
			Block block2 = loc2.getBlock();
			if (block.getType() == Material.AIR
					&& block2.getType() == Material.AIR) {
				if (hook.worldGuard()){
					hook.wgHook();
					if (hook.wg.canBuild(player, block)){
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String failMessage() {
		return ChatColor.GRAY + "Cannot blink there!";
	}
	
}

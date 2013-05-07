package minny.zephyrus.spells;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import minny.zephyrus.Zephyrus;
import minny.zephyrus.utils.ParticleEffects;

public class Blink extends Spell {

	public Blink(Zephyrus plugin) {
		super(plugin);
		plugin.spellMap.put(this.name(), this);
		plugin.spellCraftMap.put(this.spellItem(), this);
	}

	@Override
	public String name() {
		return "blink";
	}

	@Override
	public int reqLevel() {
		return 2;
	}

	@Override
	public int manaCost() {
		return 40;
	}

	@Override
	public ItemStack spellItem(){
		return new ItemStack(Material.EYE_OF_ENDER);
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
			Location loc = player.getTargetBlock(null, 100).getLocation();
			loc.setY(loc.getY() + 1);
			Location loc2 = loc;
			loc2.setY(loc2.getY() + 1);
			Block block = loc.getBlock();
			Block block2 = loc2.getBlock();
			if (block.getType() == Material.AIR
					&& block2.getType() == Material.AIR) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String failMessage() {
		return ChatColor.GRAY + "Cannot blink there!";
	}

}

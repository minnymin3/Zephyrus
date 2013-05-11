package minny.zephyrus.spells;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import minny.zephyrus.Zephyrus;

public class Frenzy extends Spell{

	public Frenzy(Zephyrus plugin) {
		super(plugin);
	}

	@Override
	public String name() {
		return "frenzy";
	}

	@Override
	public String bookText() {
		return "Better confusion. HUGE range!";
	}

	@Override
	public int reqLevel() {
		return 3;
	}

	@Override
	public int manaCost() {
		return 250;
	}

	@Override
	public void run(Player player) {
		Monster[] e = getNearbyEntities(player.getLocation(), 24);
		for (int i = 0; i < e.length; i++) {
			int index = i + 1;
			if (index >= e.length) {
				index = 0;
			}
			e[i].setTarget(e[index]);
			CraftLivingEntity m = (CraftLivingEntity) e[i];
			CraftLivingEntity tar = (CraftLivingEntity) e[index];
			m.getHandle().setGoalTarget(tar.getHandle());
		}
	}

	@Override
	public boolean canRun(Player player) {
		try {
			@SuppressWarnings("unused")
			CraftLivingEntity test = new CraftLivingEntity(null, null);
			return true;
		} catch (NoClassDefFoundError err) {
			return false;
		}
	}

	@Override
	public String failMessage() {
		return ChatColor.RED + "Zephyrus is not fully compatible with this version of Bukkit. This spell has been disabled :(";
	}
	
	@Override
	public Set<ItemStack> spellItems() {
		Set<ItemStack> i = new HashSet<ItemStack>();
		i.add(new ItemStack(Material.ENDER_PEARL));
		i.add(new ItemStack(Material.STRING));
		i.add(new ItemStack(Material.SULPHUR));
		i.add(new ItemStack(Material.ROTTEN_FLESH));
		i.add(new ItemStack(Material.BONE));
		i.add(new ItemStack(Material.GOLD_INGOT));
		return i;
	}

	@Override
	public Spell reqSpell(){
		Confuse c = new Confuse(plugin);
		return c;
	}
	
	public static Monster[] getNearbyEntities(Location l, int radius) {
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
		HashSet<Entity> radiusEntities = new HashSet<Entity>();
		for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
			for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
				int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
				for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z
						+ (chZ * 16)).getChunk().getEntities()) {
					if (e.getLocation().distance(l) <= radius
							&& e.getLocation().getBlock() != l.getBlock())
						if (e instanceof LivingEntity){
							radiusEntities.add(e);
						}
				}
			}
		}
		return radiusEntities.toArray(new Monster[radiusEntities.size()]);
	}
}

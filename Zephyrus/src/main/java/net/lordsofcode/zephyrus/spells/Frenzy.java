package net.lordsofcode.zephyrus.spells;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lordsofcode.zephyrus.api.ISpell;
import net.lordsofcode.zephyrus.api.SpellTypes.EffectType;
import net.lordsofcode.zephyrus.api.SpellTypes.Element;
import net.lordsofcode.zephyrus.api.SpellTypes.Priority;
import net.lordsofcode.zephyrus.utils.Effects;
import net.lordsofcode.zephyrus.utils.Lang;
import net.lordsofcode.zephyrus.utils.ParticleEffects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Zephyrus
 * 
 * @author minnymin3
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */

public class Frenzy extends Spell {

	@Override
	public String getName() {
		return "frenzy";
	}

	@Override
	public String getDesc() {
		return "Better confusion. Bigger range!";
	}

	@Override
	public int reqLevel() {
		return 5;
	}

	@Override
	public int manaCost() {
		return 50;
	}

	@Override
	public boolean canBind() {
		return true;
	}

	@Override
	public boolean run(Player player, String[] args) {
		try {
			new CraftLivingEntity(null, null);
		} catch (NoClassDefFoundError err) {
			Lang.errMsg("outofdate", player);
			return false;
		}
		int r = getConfig().getInt(getName() + ".radius");
		Monster[] e = getNearbyEntities(player.getLocation(), r);
		for (int i = 0; i < e.length; i++) {
			int index = i + 1;
			if (index >= e.length) {
				index = 0;
			}
			e[i].setTarget(e[index]);
			CraftCreature m = (CraftCreature) e[i];
			CraftLivingEntity tar = (CraftLivingEntity) e[index];
			m.getHandle().setGoalTarget(tar.getHandle());
			Location loc = m.getLocation();
			loc.setY(loc.getY() + 1);
			Effects.playEffect(ParticleEffects.ANGRY_VILLAGER, loc, 0.25F, 0.25F, 0.25F, 5, 10);
		}
		return true;
	}

	@Override
	public Map<String, Object> getConfiguration() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("radius", 8);
		return map;
	}

	@Override
	public Set<ItemStack> items() {
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
	public ISpell getRequiredSpell() {
		return Spell.forName("confuse");
	}

	private static Monster[] getNearbyEntities(Location l, int radius) {
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
		HashSet<Monster> radiusEntities = new HashSet<Monster>();
		for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
			for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
				int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
				for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
					if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) {
						if (e instanceof Monster) {
							radiusEntities.add((Monster) e);
						}
					}
				}
			}
		}
		return radiusEntities.toArray(new Monster[radiusEntities.size()]);
	}

	@Override
	public EffectType getPrimaryType() {
		return EffectType.ATTACK;
	}

	@Override
	public Element getElementType() {
		return Element.GENERIC;
	}

	@Override
	public Priority getPriority() {
		return Priority.MEDIUM;
	}

	@Override
	public boolean sideEffect(Player player, String[] args) {
		return false;
	}

}

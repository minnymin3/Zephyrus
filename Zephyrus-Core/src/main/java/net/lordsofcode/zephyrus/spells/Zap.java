package net.lordsofcode.zephyrus.spells;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lordsofcode.zephyrus.api.ISpell;
import net.lordsofcode.zephyrus.api.SpellTypes.Type;
import net.lordsofcode.zephyrus.api.SpellTypes.Element;
import net.lordsofcode.zephyrus.api.SpellTypes.Priority;
import net.lordsofcode.zephyrus.utils.Lang;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Zephyrus
 * 
 * @author minnymin3
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */

public class Zap extends Spell {

	public Zap() {
		Lang.add("spells.zap.fail", "You don't have a valid target!");
	}

	@Override
	public String getName() {
		return "zap";
	}

	@Override
	public String getDesc() {
		return "Brings down a bolt of lightning upon your enemy that will jump to other nearby enemies";
	}

	@Override
	public int reqLevel() {
		return 12;
	}

	@Override
	public int manaCost() {
		return 1024;
	}

	@Override
	public boolean run(Player player, String[] args, int power) {
		Entity e = getTarget(player);
		if (e instanceof LivingEntity) {
			Set<Entity> list = new HashSet<Entity>();
			int radius = getConfig().getInt(getName() + ".radius");
			radius *= power;
			int limit = getConfig().getInt(getName() + ".limit");
			LivingEntity en = (LivingEntity) e;
			en.getWorld().strikeLightning(e.getLocation());
			list.add(e);
			loopThrough(e.getNearbyEntities(radius, radius, radius), player, list, radius, limit);
			return true;
		}
		Lang.errMsg("spells.zap.fail", player);
		return false;
	}

	public void loopThrough(List<Entity> e, Player player, Set<Entity> list, int radius, int limit) {
		for (Entity en : e) {
			if (en instanceof LivingEntity && en != player && en.getLocation().distance(player.getLocation()) < limit
					&& !list.contains(en)) {
				en.getWorld().strikeLightning(en.getLocation());
				list.add(en);
				loopThrough(en.getNearbyEntities(radius, radius, radius), player, list, radius, limit);
			}
		}
	}

	@Override
	public Map<String, Object> getConfiguration() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("radius", 8);
		map.put("limit", 50);
		return map;
	}

	@Override
	public Set<ItemStack> items() {
		Set<ItemStack> i = new HashSet<ItemStack>();
		i.add(new ItemStack(Material.FLINT_AND_STEEL));
		i.add(new ItemStack(Material.DIAMOND, 2));
		i.add(new ItemStack(Material.EMERALD, 4));
		return i;
	}

	@Override
	public ISpell getRequiredSpell() {
		return Spell.forName("smite");
	}

	@Override
	public Type getPrimaryType() {
		return Type.ATTACK;
	}

	@Override
	public Element getElementType() {
		return Element.LIGHT;
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

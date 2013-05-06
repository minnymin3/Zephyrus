package minny.zephyrus.spells;

import minny.zephyrus.Zephyrus;

import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

public class Fireball extends Spell {

	public Fireball(Zephyrus plugin) {
		super(plugin);
		plugin.spellMap.put(this.name(), this);
	}

	@Override
	public String name() {
		return "fireball";
	}

	@Override
	public int reqLevel() {
		return 0;
	}

	@Override
	public int manaCost() {
		return 50;
	}

	@Override
	public void run(Player player) {
		player.launchProjectile(SmallFireball.class);
	}

}

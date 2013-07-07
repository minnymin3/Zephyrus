package net.lordsofcode.zephyrus.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Zephyrus
 * 
 * @author minnymin3
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */

public enum ParticleEffects {

	BIG_EXPLOSION("hugeexplosion", 0), LARGE_EXPLODE("largeexplode", 1), FIREWORK_SPARK(
			"fireworksSpark", 2), AIR_BUBBLE("bubble", 3), SUSPEND("suspend", 4), DEPTH_SUSPEND(
			"depthSuspend", 5), TOWN_AURA("townaura", 6), CRITICAL_HIT("crit", 7), MAGIC_CRITICAL_HIT(
			"magicCrit", 8), MOB_SPELL("mobSpell", 9), MOB_SPELL_AMBIENT(
			"mobSpellAmbient", 10), SPELL("spell", 11), INSTANT_SPELL(
			"instantSpell", 12), BLUE_SPARKLE("witchMagic", 13), NOTE_BLOCK("note", 14), ENDER(
			"portal", 15), ENCHANTMENT_TABLE("enchantmenttable", 16), EXPLODE(
			"explode", 17), FIRE("flame", 18), LAVA_SPARK("lava", 19), FOOTSTEP(
			"footstep", 20), SPLASH("splash", 21), LARGE_SMOKE("largesmoke", 22), CLOUD(
			"cloud", 23), REDSTONE_DUST("reddust", 24), SNOWBALL_HIT(
			"snowballpoof", 25), DRIP_WATER("dripWater", 26), DRIP_LAVA(
			"dripLava", 27), SNOW_DIG("snowshovel", 28), SLIME("slime", 29), HEART(
			"heart", 30), ANGRY_VILLAGER("angryVillager", 31), GREEN_SPARKLE(
			"happyVillager", 32), ICONCRACK("iconcrack", 33), TILECRACK(
			"tilecrack", 34);

	private String name;
	private int id;

	ParticleEffects(String name, int id) {
		this.name = name;
		this.id = id;
	}

	/**
	 * Gets the name of the Particle Effect
	 * 
	 * @return The particle effect name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the id of the Particle Effect
	 * 
	 * @return The id of the Particle Effect
	 */
	public int getId() {
		return id;
	}

	/**
	 * Send a particle effect to a player
	 * 
	 * @param effect
	 *            The particle effect to send
	 * @param player
	 *            The player to send the effect to
	 * @param location
	 *            The location to send the effect to
	 * @param offsetX
	 *            The x range of the particle effect
	 * @param offsetY
	 *            The y range of the particle effect
	 * @param offsetZ
	 *            The z range of the particle effect
	 * @param speed
	 *            The speed (or color depending on the effect) of the particle
	 *            effect
	 * @param count
	 *            The count of effects
	 */
	public static void sendToPlayer(ParticleEffects effect, Player player,
			Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int count) {
		Object packet;
		try {
			packet = createPacket(effect, location, offsetX, offsetY, offsetZ,
					speed, count);
			sendPacket(player, packet);
		} catch (Exception e) {
		}

	}

	/**
	 * Send a particle effect to all players
	 * 
	 * @param effect
	 *            The particle effect to send
	 * @param location
	 *            The location to send the effect to
	 * @param offsetX
	 *            The x range of the particle effect
	 * @param offsetY
	 *            The y range of the particle effect
	 * @param offsetZ
	 *            The z range of the particle effect
	 * @param speed
	 *            The speed (or color depending on the effect) of the particle
	 *            effect
	 * @param count
	 *            The count of effects
	 */
	public static void sendToLocation(ParticleEffects effect,
			Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int count) {
		try {
			Object packet = createPacket(effect, location, offsetX, offsetY,
					offsetZ, speed, count);
			for (Player player : Bukkit.getOnlinePlayers()) {
				sendPacket(player, packet);
			}
		} catch (Exception e) {

		}
	}

	public static void sendCrackToPlayer(boolean icon, int id, byte data,
			Player player, Location location, float offsetX, float offsetY,
			float offsetZ, int count) throws Exception {
		Object packet = createCrackPacket(icon, id, data, location, offsetX,
				offsetY, offsetZ, count);
		sendPacket(player, packet);
	}

	public static void sendCrackToLocation(boolean icon, int id, byte data,
			Location location, float offsetX, float offsetY, float offsetZ,
			int count) throws Exception {
		Object packet = createCrackPacket(icon, id, data, location, offsetX,
				offsetY, offsetZ, count);
		for (Player player : Bukkit.getOnlinePlayers()) {
			sendPacket(player, packet);
		}
	}

	private static Object createPacket(ParticleEffects effect,
			Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int count) throws Exception {
		if (count <= 0) {
			count = 1;
		}
		Object packet = getPacket63WorldParticles();
		setValue(packet, "a", effect.name);
		setValue(packet, "b", (float) location.getX());
		setValue(packet, "c", (float) location.getY());
		setValue(packet, "d", (float) location.getZ());
		setValue(packet, "e", offsetX);
		setValue(packet, "f", offsetY);
		setValue(packet, "g", offsetZ);
		setValue(packet, "h", speed);
		setValue(packet, "i", count);
		return packet;
	}

	private static Object createCrackPacket(boolean icon, int id, byte data,
			Location location, float offsetX, float offsetY, float offsetZ,
			int count) throws Exception {
		if (count <= 0) {
			count = 1;
		}
		Object packet = getPacket63WorldParticles();
		String modifier = "iconcrack_" + id;
		if (!icon) {
			modifier = "tilecrack_" + id + "_" + data;
		}
		setValue(packet, "a", modifier);
		setValue(packet, "b", (float) location.getX());
		setValue(packet, "c", (float) location.getY());
		setValue(packet, "d", (float) location.getZ());
		setValue(packet, "e", offsetX);
		setValue(packet, "f", offsetY);
		setValue(packet, "g", offsetZ);
		setValue(packet, "h", 0.1F);
		setValue(packet, "i", count);
		return packet;
	}

	private static void setValue(Object instance, String fieldName, Object value)
			throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(instance, value);
	}

	private static Object getEntityPlayer(Player p) throws Exception {
		Method getHandle = p.getClass().getMethod("getHandle");
		return getHandle.invoke(p);
	}

	private static String getPackageName() {
		return "net.minecraft.server."
				+ Bukkit.getServer().getClass().getPackage().getName()
						.replace(".", ",").split(",")[3];
	}

	private static Object getPacket63WorldParticles() throws Exception {
		Class<?> packet = Class.forName(getPackageName()
				+ ".Packet63WorldParticles");
		return packet.getConstructors()[0].newInstance();
	}

	private static void sendPacket(Player p, Object packet) throws Exception {
		Object eplayer = getEntityPlayer(p);
		Field playerConnectionField = eplayer.getClass().getField(
				"playerConnection");
		Object playerConnection = playerConnectionField.get(eplayer);
		for (Method m : playerConnection.getClass().getMethods()) {
			if (m.getName().equalsIgnoreCase("sendPacket")) {
				m.invoke(playerConnection, packet);
				return;
			}
		}
	}
}
package net.lordsofcode.zephyrus.commands;

import net.lordsofcode.zephyrus.Zephyrus;
import net.lordsofcode.zephyrus.api.ICustomItem;
import net.lordsofcode.zephyrus.items.ItemUtil;
import net.lordsofcode.zephyrus.utils.Lang;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Zephyrus
 * 
 * @author minnymin3
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */

public class LevelUpItem extends ItemUtil implements CommandExecutor {

	public LevelUpItem() {
		Lang.add("itemlevel.nomore", "That item cannot be leveled any more!");
		Lang.add("itemlevel.cantlevel", "The item you are holding cannot be leveled!");
		Lang.add("itemlevel.complete", "You have leveled up the [ITEMNAME]");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("zephyrus.levelup.item") || player.isOp()) {
				if (Zephyrus.getItemManager().isCustomItem(player.getItemInHand())) {
					ICustomItem i = Zephyrus.getItemManager().getCustomItem(player.getItemInHand());
					if (getItemLevel(player.getItemInHand()) < i.getMaxLevel()) {
						int current = getItemLevel(player.getItemInHand());
						setItemLevel(player.getItemInHand(), current + 1);
						sender.sendMessage(Lang.get("itemlevel.complete").replace("[ITEMNAME]",
								Lang.caps(i.getDisplayName())));
					} else {
						Lang.errMsg("itemlevel.nomore", sender);
					}
				} else {
					Lang.errMsg("cantlevel", sender);
				}
			} else {
				Lang.errMsg("noperm", sender);
			}
		} else {
			Lang.errMsg("ingameonly", sender);
		}
		return false;
	}
}
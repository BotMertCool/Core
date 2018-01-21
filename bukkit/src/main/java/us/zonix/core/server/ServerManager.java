package us.zonix.core.server;

import us.zonix.core.CorePlugin;
import us.zonix.core.profile.Profile;
import us.zonix.core.rank.Rank;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import us.zonix.core.util.BungeeUtil;
import us.zonix.core.util.ItemUtil;
import us.zonix.core.util.inventory.InventoryUI;

public class ServerManager {

	private final CorePlugin plugin = CorePlugin.getInstance();

	@Getter
	private final InventoryUI serverSelector = new InventoryUI("Server Selector", true, 1);

	public ServerManager() {
		this.setupInventories();
		this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::updateInventories, 20L, 20L);
	}

	private void setupInventories() {

		this.serverSelector.setItem(3, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.GOLD.toString() + ChatColor.BOLD + " US")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-us");
				player.closeInventory();
			}
		});

		this.serverSelector.setItem(5, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.GOLD_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.GOLD.toString() + ChatColor.BOLD + " EU")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-eu");
				player.closeInventory();
			}
		});
	}

	private void updateInventories() {
		for (int i = 0; i < 18; i++) {
			InventoryUI.ClickableItem item = this.serverSelector.getItem(i);
			if (item != null) {

				if(item.getItemStack().getType() == Material.DIAMOND_SWORD) {
					item.setItemStack(this.updateQueueLore(item.getItemStack(), "practice-us"));
				} else if(item.getItemStack().getType() == Material.GOLD_SWORD) {
					item.setItemStack(this.updateQueueLore(item.getItemStack(), "practice-eu"));
				}

				this.serverSelector.setItem(i, item);
			}
		}
	}

	private ItemStack updateQueueLore(ItemStack itemStack, String server) {
		if (itemStack == null) {
			return null;
		}

		ServerData serverData = this.plugin.getRedisManager().getServerDataByName(server);

		if(serverData == null) {
			return ItemUtil.reloreItem(itemStack, ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------", ChatColor.GOLD.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Players: " + ChatColor.GRAY + "(" + 0 + "/" + 0 + ")", ChatColor.GOLD.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Status: " + ChatColor.RED + "Offline", ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------");
		}

		return ItemUtil.reloreItem(itemStack, ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------", ChatColor.GOLD.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Players: " + ChatColor.GRAY + "(" + serverData.getOnlinePlayers() + "/" + serverData.getMaxPlayers() + ")", ChatColor.GOLD.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Status: " + (serverData.isWhitelisted() ? ChatColor.YELLOW + "Whitelisted" : ChatColor.GREEN + "Online"), ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------");
	}

}

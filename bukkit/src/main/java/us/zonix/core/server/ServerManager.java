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

	@Getter
	private final InventoryUI practiceSelector = new InventoryUI("Server Selector", true, 1);

	@Getter
	private final InventoryUI hcfSelector = new InventoryUI("Server Selector", true, 1);

	@Getter
	private final InventoryUI kitMapSelector = new InventoryUI("Server Selector", true, 1);

	@Getter
	private final InventoryUI soupSelector = new InventoryUI("Server Selector", true, 1);

	public ServerManager() {
		this.setupMainInventory();
		this.setupPracticeInventory();
		this.setupHCFInventory();
		this.setupKitMapInventory();
		this.setupSoupInventory();
		this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::updateInventories, 20L, 20L);
	}

	private void setupMainInventory() {
		this.serverSelector.setItem(1, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.BOW, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				player.openInventory(practiceSelector.getCurrentPage());
			}
		});

		this.serverSelector.setItem(3, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(hcfSelector.getCurrentPage());
			}
		});

		this.serverSelector.setItem(5, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(kitMapSelector.getCurrentPage());
			}
		});

		this.serverSelector.setItem(7, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(soupSelector.getCurrentPage());
			}
		});
	}

	private void setupPracticeInventory() {
		this.practiceSelector.setItem(1, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.BOW, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " US")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-us");
				player.closeInventory();
			}
		});

		this.practiceSelector.setItem(3, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.BOW, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " EU")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-eu");
				player.closeInventory();
			}
		});

		this.practiceSelector.setItem(5, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.BOW, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " SA")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-sa");
				player.closeInventory();
			}
		});

		this.practiceSelector.setItem(7, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.BOW, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " AS")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.sendMessage(ChatColor.RED + "Please connect using as.zonix.us");
				player.closeInventory();
			}
		});
	}

	private void setupHCFInventory() {
		this.hcfSelector.setItem(1, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " US")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "hcf-us");
				player.closeInventory();
			}
		});

		this.hcfSelector.setItem(3, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " EU")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "hcf-eu");
				player.closeInventory();
			}
		});

		this.hcfSelector.setItem(5, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " SA")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "hcf-sa");
				player.closeInventory();
			}
		});

		this.hcfSelector.setItem(7, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " AS")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "hcf-sa");
				player.closeInventory();
			}
		});
	}

	private void setupKitMapInventory() {
		this.kitMapSelector.setItem(1, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " US")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "kitmap-us");
				player.closeInventory();
			}
		});

		this.kitMapSelector.setItem(3, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " EU")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "kitmap-eu");
				player.closeInventory();
			}
		});

		this.kitMapSelector.setItem(5, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " SA")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "kitmap-sa");
				player.closeInventory();
			}
		});

		this.kitMapSelector.setItem(7, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " AS")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "kitmap-as");
				player.closeInventory();
			}
		});
	}

	private void setupSoupInventory() {
		this.soupSelector.setItem(1, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " US")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "soup-us");
				player.closeInventory();
			}
		});

		this.soupSelector.setItem(3, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " EU")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "soup-eu");
				player.closeInventory();
			}
		});

		this.soupSelector.setItem(5, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " SA")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "soup-sa");
				player.closeInventory();
			}
		});

		this.soupSelector.setItem(7, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " AS")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				//player.performCommand("joinqueue " + "soup-as");
				player.closeInventory();
			}
		});
	}

	private void updateInventories() {
		for (int i = 0; i < 18; i++) {

			InventoryUI.ClickableItem item = this.practiceSelector.getItem(i);

			if (item != null) {

				String serverName = ChatColor.stripColor(item.getItemStack().getItemMeta().getDisplayName()).split(" ")[2];
				item.setItemStack(this.updateQueueLore(item.getItemStack(), "practice-" + serverName.toLowerCase()));

				this.practiceSelector.setItem(i, item);
			}
		}
	}

	private ItemStack updateQueueLore(ItemStack itemStack, String server) {
		if (itemStack == null) {
			return null;
		}

		if(server.equalsIgnoreCase("practice-as")) {
			return ItemUtil.reloreItem(itemStack, ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------", ChatColor.RED.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Players: " + ChatColor.GRAY + "(" + this.plugin.getRedisManager().getAsiaPlayerCount() + "/" + 500 + ")", ChatColor.RED.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Online", ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------");
		}

		ServerData serverData = this.plugin.getRedisManager().getServerDataByName(server);

		if (serverData == null) {
			return ItemUtil.reloreItem(itemStack, ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------", ChatColor.RED.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Players: " + ChatColor.GRAY + "(" + 0 + "/" + 0 + ")", ChatColor.RED.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Status: " + ChatColor.RED + "Offline", ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------");
		}

		return ItemUtil.reloreItem(itemStack, ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------", ChatColor.RED.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Players: " + ChatColor.GRAY + "(" + serverData.getOnlinePlayers() + "/" + serverData.getMaxPlayers() + ")", ChatColor.RED.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Status: " + (serverData.isWhitelisted() ? ChatColor.DARK_RED + "Whitelisted" : ChatColor.GREEN + "Online"), ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH +  "-----------------");
	}

}

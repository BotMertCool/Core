package us.zonix.core.server;

import us.zonix.core.CorePlugin;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import us.zonix.core.util.ItemUtil;
import us.zonix.core.util.inventory.InventoryUI;

public class ServerManager {

	private final CorePlugin plugin = CorePlugin.getInstance();

	@Getter
	private final InventoryUI practiceSelector = new InventoryUI("Server Selector", true, 5);

	@Getter
	private final InventoryUI hcfSelector = new InventoryUI("Server Selector", true, 5);

	@Getter
	private final InventoryUI kitMapSelector = new InventoryUI("Server Selector", true, 5);

	@Getter
	private final InventoryUI soupSelector = new InventoryUI("Server Selector", true, 5);

	public ServerManager() {
		this.setupPracticeInventory();
		this.setupHCFInventory();
		this.setupKitMapInventory();
		this.setupSoupInventory();
		this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::updateInventories, 20L, 20L);
	}

	private void setupPracticeInventory() {

		this.practiceSelector.setItem(10, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " US", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-us");
				player.closeInventory();
			}
		});

		this.practiceSelector.setItem(12, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " EU", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-eu");
				player.closeInventory();
			}
		});

		this.practiceSelector.setItem(14, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " SA", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-sa");
				player.closeInventory();
			}
		});

		this.practiceSelector.setItem(16, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " AS", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.sendMessage(ChatColor.RED + "Please connect using as.zonix.us");
				player.closeInventory();
			}
		});

		this.practiceSelector.setItem(29, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				player.openInventory(hcfSelector.getCurrentPage());
			}
		});

		this.practiceSelector.setItem(31, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				player.openInventory(kitMapSelector.getCurrentPage());
			}
		});

		this.practiceSelector.setItem(33, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(soupSelector.getCurrentPage());
			}
		});

		int[] insideSlots = new int[] {11, 13, 15, 19, 20, 21, 22, 23, 24, 25, 28, 30, 32, 34};
		int[] outsideSlots = new int[] {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44};

		for(int i : insideSlots) {
			this.practiceSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 7)) {@Override public void onClick(InventoryClickEvent event) {}});
		}

		for(int i : outsideSlots) {
			this.practiceSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 14)) {@Override public void onClick(InventoryClickEvent event) {}});
		}

	}

	private void setupHCFInventory() {

		this.hcfSelector.setItem(10, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " US")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "hcf-us");
				player.closeInventory();
			}
		});

		this.hcfSelector.setItem(12, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 14)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
			}
		});

		this.hcfSelector.setItem(14, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " SA")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "hcf-sa");
				player.closeInventory();
			}
		});

		this.hcfSelector.setItem(16, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " AS")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.sendMessage(ChatColor.RED + "Please connect using as.zonix.us");
				player.closeInventory();
			}
		});

		this.hcfSelector.setItem(29, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "(Click to view servers)", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				player.openInventory(practiceSelector.getCurrentPage());
			}
		});

		this.hcfSelector.setItem(31, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				player.openInventory(kitMapSelector.getCurrentPage());
			}
		});

		this.hcfSelector.setItem(33, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(soupSelector.getCurrentPage());
			}
		});

		int[] insideSlots = new int[] {11, 13, 15, 19, 20, 21, 22, 23, 24, 25, 28, 30, 32, 34};
		int[] outsideSlots = new int[] {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44};

		for(int i : insideSlots) {
			this.hcfSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 7)) {@Override public void onClick(InventoryClickEvent event) {}});
		}

		for(int i : outsideSlots) {
			this.hcfSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 14)) {@Override public void onClick(InventoryClickEvent event) {}});
		}
	}

	private void setupKitMapInventory() {

		this.kitMapSelector.setItem(10, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " US")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "kitmap-us");
				player.closeInventory();
			}
		});

		this.kitMapSelector.setItem(12, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " EU")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "kitmap-eu");
				player.closeInventory();
			}
		});

		this.kitMapSelector.setItem(14, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " SA")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "kitmap-sa");
				player.closeInventory();
			}
		});

		this.kitMapSelector.setItem(16, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " AS")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.sendMessage(ChatColor.RED + "Please connect using as.zonix.us");
				player.closeInventory();
			}
		});

		this.kitMapSelector.setItem(29, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "(Click to view servers)", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				player.openInventory(practiceSelector.getCurrentPage());
			}
		});

		this.kitMapSelector.setItem(31, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(hcfSelector.getCurrentPage());
			}
		});

		this.kitMapSelector.setItem(33, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(soupSelector.getCurrentPage());
			}
		});

		int[] insideSlots = new int[] {11, 13, 15, 19, 20, 21, 22, 23, 24, 25, 28, 30, 32, 34};
		int[] outsideSlots = new int[] {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44};

		for(int i : insideSlots) {
			this.kitMapSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 7)) {@Override public void onClick(InventoryClickEvent event) {}});
		}

		for(int i : outsideSlots) {
			this.kitMapSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 14)) {@Override public void onClick(InventoryClickEvent event) {}});
		}
	}

	private void setupSoupInventory() {

		this.soupSelector.setItem(10, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " US")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "soup-us");
				player.closeInventory();
			}
		});

		this.soupSelector.setItem(12, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " EU")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "soup-eu");
				player.closeInventory();
			}
		});

		this.soupSelector.setItem(14, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.MUSHROOM_SOUP, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " SA")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "soup-sa");
				player.closeInventory();
			}
		});

		this.soupSelector.setItem(16, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Soup PvP " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " AS")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.sendMessage(ChatColor.RED + "Please connect using as.zonix.us");
				player.closeInventory();
			}
		});

		this.soupSelector.setItem(29, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "(Click to view servers)", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(hcfSelector.getCurrentPage());
			}
		});

		this.soupSelector.setItem(31, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(kitMapSelector.getCurrentPage());
			}
		});

		this.soupSelector.setItem(33, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap " + ChatColor.GRAY + "(Click to view servers)")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				//player.openInventory(soupSelector.getCurrentPage());
			}
		});

		int[] insideSlots = new int[] {11, 13, 15, 19, 20, 21, 22, 23, 24, 25, 28, 30, 32, 34};
		int[] outsideSlots = new int[] {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44};

		for(int i : insideSlots) {
			this.soupSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 7)) {@Override public void onClick(InventoryClickEvent event) {}});
		}

		for(int i : outsideSlots) {
			this.soupSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 14)) {@Override public void onClick(InventoryClickEvent event) {}});
		}
	}

	private void updateInventories() {
		for (int i = 0; i < 18; i++) {

			InventoryUI.ClickableItem item = this.practiceSelector.getItem(i);

			if (item != null && item.getItemStack().getType() == Material.POTION) {

				String serverName = ChatColor.stripColor(item.getItemStack().getItemMeta().getDisplayName()).split(" ")[2];
				item.setItemStack(this.updateQueueLore(item.getItemStack(), "practice-" + serverName.toLowerCase()));

				this.practiceSelector.setItem(i, item);
			}

			item = this.hcfSelector.getItem(i);

			if (item != null && item.getItemStack().getType() == Material.DIAMOND_SWORD) {

				String serverName = ChatColor.stripColor(item.getItemStack().getItemMeta().getDisplayName()).split(" ")[2];
				item.setItemStack(this.updateQueueLore(item.getItemStack(), "hcf-" + serverName.toLowerCase()));

				this.hcfSelector.setItem(i, item);
			}

			item = this.kitMapSelector.getItem(i);

			if (item != null && item.getItemStack().getType() == Material.ENDER_CHEST) {

				String serverName = ChatColor.stripColor(item.getItemStack().getItemMeta().getDisplayName()).split(" ")[2];
				item.setItemStack(this.updateQueueLore(item.getItemStack(), "kitmap-" + serverName.toLowerCase()));

				this.kitMapSelector.setItem(i, item);
			}

			item = this.soupSelector.getItem(i);

			if (item != null && item.getItemStack().getType() == Material.MUSHROOM_SOUP) {

				String serverName = ChatColor.stripColor(item.getItemStack().getItemMeta().getDisplayName()).split(" ")[3];
				item.setItemStack(this.updateQueueLore(item.getItemStack(), "soup-" + serverName.toLowerCase()));

				this.soupSelector.setItem(i, item);
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

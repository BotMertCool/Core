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
	private final InventoryUI serverSelector = new InventoryUI("Server Selector", true, 5);
	public ServerManager() {
		this.setupServerSelector();
		this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::updateInventories, 20L, 20L);
	}

	private void setupServerSelector() {

		this.serverSelector.setItem(10, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " US", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-us");
				player.closeInventory();
			}
		});

		this.serverSelector.setItem(12, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " EU", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-eu");
				player.closeInventory();
			}
		});

		this.serverSelector.setItem(14, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " SA", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "practice-sa");
				player.closeInventory();
			}
		});

		this.serverSelector.setItem(16, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.POTION, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice " + ChatColor.GRAY + "|" + ChatColor.RED.toString() + ChatColor.BOLD + " AS", 1, (short) 8197)) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.sendMessage(ChatColor.RED + "Please connect using as.zonix.us");
				player.closeInventory();
			}
		});

		this.serverSelector.setItem(29, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "hcf-us");
				player.closeInventory();
			}
		});

		this.serverSelector.setItem(31, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.ENDER_CHEST, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "KitMap")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "kitmap-us");
				player.closeInventory();
			}
		});

		this.serverSelector.setItem(33, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.TNT, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Factions")) {
			@Override
			public void onClick(InventoryClickEvent event) {
				Player player = (Player) event.getWhoClicked();
				player.performCommand("joinqueue " + "factions-us");
				player.closeInventory();
			}
		});

		int[] insideSlots = new int[] {11, 13, 15, 19, 20, 21, 22, 23, 24, 25, 28, 30, 32, 34};
		int[] outsideSlots = new int[] {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44};

		for(int i : insideSlots) {
			this.serverSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 7)) {@Override public void onClick(InventoryClickEvent event) {}});
		}

		for(int i : outsideSlots) {
			this.serverSelector.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.STAINED_GLASS_PANE, " ", 1, (short) 14)) {@Override public void onClick(InventoryClickEvent event) {}});
		}

	}


	private void updateInventories() {
		for (int i = 0; i < 33; i++) {

			InventoryUI.ClickableItem item = this.serverSelector.getItem(i);

			if (item != null && item.getItemStack().getType() == Material.POTION) {

				String serverName = ChatColor.stripColor(item.getItemStack().getItemMeta().getDisplayName()).split(" ")[2];
				item.setItemStack(this.updateQueueLore(item.getItemStack(), "practice-" + serverName.toLowerCase()));

				this.serverSelector.setItem(i, item);
			}

			if (item != null && item.getItemStack().getType() == Material.DIAMOND_SWORD) {
				item.setItemStack(this.updateQueueLore(item.getItemStack(), "hcf-us"));
				this.serverSelector.setItem(i, item);
			}

			if (item != null && item.getItemStack().getType() == Material.ENDER_CHEST) {
				item.setItemStack(this.updateQueueLore(item.getItemStack(), "kitmap-us"));
				this.serverSelector.setItem(i, item);
			}

			if (item != null && item.getItemStack().getType() == Material.TNT) {
				item.setItemStack(this.updateQueueLore(item.getItemStack(), "factions-us"));
				this.serverSelector.setItem(i, item);
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

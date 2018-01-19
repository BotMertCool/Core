package us.zonix.core.server.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import us.zonix.core.CorePlugin;
import us.zonix.core.util.ItemUtil;


public class ServerListeners implements Listener {

    private ItemStack[] items = new ItemStack[]{
                null,
                null,
                null,
                null,
                ItemUtil.createItem(Material.COMPASS, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Server Selector"),
                null,
                null,
                null,
                null
    };

    @EventHandler
    public void onLoginPrevention(PlayerLoginEvent event) {

        if(event.getAddress().getHostAddress().equalsIgnoreCase("127.0.0.1")) {
            event.setKickMessage(ChatColor.RED + "You are not allowed to connect.");
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer();

        event.setJoinMessage(null);

        for (int i = 0; i <= 100; i++) {
            player.sendMessage(" ");
        }

        player.getInventory().clear();
        player.getInventory().setContents(this.items);
        player.setHealth(20F);

        if(CorePlugin.getInstance().getSpawnLocation() != null) {
            player.teleport(CorePlugin.getInstance().getSpawnLocation());
        }

        player.getInventory().setHeldItemSlot(4);


    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack().clone();
        item.setAmount(player.getInventory().getItemInHand().getAmount() + 1);
        event.getItemDrop().remove();
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), item);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupArmour(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHealthChange(EntityRegainHealthEvent event) {
        if(event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        if(CorePlugin.getInstance().getSpawnLocation() == null) {
            return;
        }

        event.getPlayer().teleport(CorePlugin.getInstance().getSpawnLocation());
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.getDrops().clear();
    }

    @EventHandler
    public void onEntityDamager(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onVoidTouch(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Location location = player.getLocation();


        if (location.getBlockY() <= 0){

            if(CorePlugin.getInstance().getSpawnLocation() == null) {
                return;
            }

            player.teleport(CorePlugin.getInstance().getSpawnLocation());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if(event.getPlayer().isOp()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if(event.getPlayer().isOp()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        if (player.getItemInHand() == null) return;
        if (!player.getItemInHand().hasItemMeta()) return;
        if (!player.getItemInHand().getItemMeta().hasDisplayName()) return;

        if (player.getItemInHand().getType() == Material.COMPASS) {
            player.openInventory(CorePlugin.getInstance().getServerManager().getServerSelector().getCurrentPage());
        }
    }
}

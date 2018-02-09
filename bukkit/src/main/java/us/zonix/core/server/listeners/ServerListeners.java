package us.zonix.core.server.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import us.zonix.core.profile.Profile;
import us.zonix.core.redis.queue.Queue;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer();

        event.setJoinMessage(null);
        

        player.getInventory().clear();
        player.getInventory().setContents(this.items);
        player.setHealth(20F);

        if(CorePlugin.getInstance().getSpawnLocation() != null) {
            player.teleport(CorePlugin.getInstance().getSpawnLocation());
        }

        player.getInventory().setHeldItemSlot(4);

        String[] message = new String[] {
                "§8§m----------------------------------------------------",
                "§7Welcome to the §c§lZonix Network",
                " ",
                "§8§l* §6§lWebsite: §7www.zonix.us",
                "§8§l* §6§lTwitter: §7twitter.com/ZonixUS",
                "§8§l* §6§lTeamspeak: §7ts.zonix.us",
                "§8§l* §6§lStore: §7store.zonix.us",
                " ",
                "§8§m----------------------------------------------------"
        };

        player.sendMessage(message);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        event.setQuitMessage(null);

        if (!CorePlugin.getInstance().getQueueManager().isServerOnline()) {
            player.sendMessage(ChatColor.RED + "Queue Server is currently under maintenance.");
            return;
        }

        final Queue queue;

        if ((queue = CorePlugin.getInstance().getQueueManager().getQueue(player)) == null) {
            player.sendMessage(ChatColor.RED + "You are not currently in a queue.");
            return;
        }

        player.sendMessage(ChatColor.RED + "You left the queue for " + queue.getServerName() + ".");
        CorePlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(CorePlugin.getInstance(), () -> queue.removeFromQueue(player));

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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        if (player.getItemInHand() == null) return;
        if (!player.getItemInHand().hasItemMeta()) return;
        if (!player.getItemInHand().getItemMeta().hasDisplayName()) return;

        if (player.getItemInHand().getType() == Material.COMPASS && Profile.getByUuid(player.getUniqueId()).isAuthenticated()) {
            player.openInventory(CorePlugin.getInstance().getServerManager().getServerSelector().getCurrentPage());
        }
    }
}

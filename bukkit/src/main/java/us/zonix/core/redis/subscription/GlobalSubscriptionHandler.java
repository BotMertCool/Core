package us.zonix.core.redis.subscription;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.profile.Profile;
import us.zonix.core.punishment.Punishment;
import us.zonix.core.punishment.PunishmentType;
import us.zonix.core.rank.Rank;
import us.zonix.core.server.ServerData;
import us.zonix.core.shared.redis.subscription.JedisSubscriptionHandler;
import us.zonix.core.util.Clickable;
import us.zonix.core.util.UUIDType;

import java.util.UUID;

public class GlobalSubscriptionHandler implements JedisSubscriptionHandler<JsonObject> {

    @Override
    public void handleMessage(JsonObject object) {
        String server = object.get("server").getAsString();
        String type = object.get("type").getAsString();
        JsonObject data = object.get("data").getAsJsonObject();

        if (type.equalsIgnoreCase("punishment")) {
            Punishment punishment = Punishment.fromJson(data);
            punishment.announce(data.get("name").getAsString(), data.get("sender").getAsString(), data.get("silent").getAsBoolean(), punishment.isRemoved());

            UUID uuid = UUIDType.fromString(data.get("uuid").getAsString());

            Profile profile = Profile.getByUuidIfAvailable(uuid);

            if (profile == null) {
                return;
            }

            // remove non updated punishment
            if (punishment.isRemoved()) {
                profile.getPunishments().removeIf((other) -> {
                    return other.getId() == punishment.getId();
                });
            }

            // add updated punishment
            profile.getPunishments().add(punishment);

            Player player = Bukkit.getPlayer(punishment.getUuid());

            if (player != null && !punishment.isRemoved() && punishment.getType() != PunishmentType.MUTE) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer(punishment.getType().getMessage());

                        if (punishment.getType() == PunishmentType.BLACKLIST) {
                            for (Player other : Bukkit.getOnlinePlayers()) {
                                if (other.getAddress().getAddress().getHostAddress().equals(player.getAddress().getAddress().getHostAddress())) {
                                    other.kickPlayer(punishment.getType().getMessage());
                                }
                            }
                        }
                    }
                }.runTask(CorePlugin.getInstance());
            }
        }
        else if (type.equalsIgnoreCase("rank")) {
            UUID uuid = UUIDType.fromString(data.get("uuid").getAsString());
            Rank rank = Rank.valueOf(data.get("rank").getAsString());

            Profile profile = Profile.getByUuidIfAvailable(uuid);

            if (profile != null) {
                profile.setRank(rank);

                Player player = Bukkit.getPlayer(profile.getUuid());

                if (player != null) {
                    player.sendMessage(ChatColor.GREEN + "Your rank has been updated to " + rank.getName() + ".");
                }

                profile.updateTabList(rank);
            }
        }

        else if (type.equalsIgnoreCase("staffchat")) {

            String name = data.get("name").getAsString();
            Rank rank = Rank.valueOf(data.get("rank").getAsString());
            String message = data.get("message").getAsString();

            for (Player player : Bukkit.getOnlinePlayers()) {
                Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

                if (profile != null && profile.getRank().isAboveOrEqual(Rank.TRIAL_MOD)) {
                    String toSend = ChatColor.AQUA + "(Staff Chat) " + rank.getPrefix() + rank.getColor() + rank.getName() + rank.getSuffix() + rank.getColor() + name + ChatColor.WHITE + ": " + message;
                    player.sendMessage(toSend);
                }
            }
        }

        else if (type.equalsIgnoreCase("report")) {

            String name = data.get("name").getAsString();
            String target = data.get("target").getAsString();
            String message = data.get("message").getAsString();

            for (Player player : Bukkit.getOnlinePlayers()) {
                Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

                if (profile != null && profile.getRank().isAboveOrEqual(Rank.TRIAL_MOD)) {
                    String toSend = ChatColor.BLUE + "(Report) " + ChatColor.GRAY + name + ChatColor.WHITE + " reported " + ChatColor.GRAY + target + ChatColor.WHITE + " for " + ChatColor.YELLOW +  message + ".";
                    player.sendMessage(toSend);
                }
            }
        }

        else if (type.equalsIgnoreCase("whitelist")) {

            String action = data.get("action").getAsString();
            String target = data.get("target").getAsString();

            if(action.equalsIgnoreCase("add")) {

                OfflinePlayer player = Bukkit.getOfflinePlayer(target);

                if(player != null) {
                    CorePlugin.getInstance().getServer().getWhitelistedPlayers().add(player);
                    CorePlugin.getInstance().getServer().reloadWhitelist();
                }

            } else if(action.equalsIgnoreCase("remove")) {

                OfflinePlayer player = Bukkit.getOfflinePlayer(target);

                if(player != null) {
                    CorePlugin.getInstance().getServer().getWhitelistedPlayers().remove(player);
                    CorePlugin.getInstance().getServer().reloadWhitelist();
                }
            } else if(action.equalsIgnoreCase("off")) {

                if(target.equalsIgnoreCase(CorePlugin.getInstance().getServerId())) {
                    CorePlugin.getInstance().getServer().setWhitelist(false);
                }
            } else if(action.equalsIgnoreCase("on")) {

                if(target.equalsIgnoreCase(CorePlugin.getInstance().getServerId())) {
                    CorePlugin.getInstance().getServer().setWhitelist(true);
                }
            }

            CorePlugin.getInstance().getLogger().info("[Whitelist] " + target + " -> " + action + ".");
        }

        else if (type.equalsIgnoreCase("request")) {

            String name = data.get("name").getAsString();
            String message = data.get("message").getAsString();

            for (Player player : Bukkit.getOnlinePlayers()) {
                Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

                if (profile != null && profile.getRank().isAboveOrEqual(Rank.TRIAL_MOD)) {
                    String toSend = ChatColor.BLUE + "(Request) " + ChatColor.GRAY + name + ChatColor.WHITE + " requested " + ChatColor.YELLOW +  message + ".";
                    player.sendMessage(toSend);
                }
            }
        }



        else if (type.equalsIgnoreCase("server-data")) {
            String serverName = data.get("server-name").getAsString();

            ServerData serverData =  CorePlugin.getInstance().getRedisManager().getServers().get(serverName);

            if (serverData == null) {
                serverData = new ServerData();
            }

            int playersOnline = data.get("player-count").getAsInt();
            int maxPlayers = data.get("player-max").getAsInt();
            boolean whitelisted = data.get("whitelisted").getAsBoolean();

            serverData.setServerName(serverName);
            serverData.setOnlinePlayers(playersOnline);
            serverData.setMaxPlayers(maxPlayers);
            serverData.setWhitelisted(whitelisted);
            serverData.setLastUpdate(System.currentTimeMillis());

            CorePlugin.getInstance().getRedisManager().getServers().put(serverName, serverData);
        }
    }

}
package us.zonix.core.redis.subscription;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.profile.Profile;
import us.zonix.core.punishment.Punishment;
import us.zonix.core.punishment.PunishmentType;
import us.zonix.core.rank.Rank;
import us.zonix.core.shared.redis.subscription.JedisSubscriptionHandler;
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
            }
        }
    }

}
package us.zonix.core.punishment;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.profile.Profile;
import us.zonix.core.shared.api.callback.Callback;
import us.zonix.core.util.UUIDType;

import java.util.StringJoiner;
import java.util.UUID;

public class HistoryHelper {

    private CommandSender sender;

    private UUID uuid;
    private String name;

    public HistoryHelper(CommandSender sender, String name) {
        this.sender = sender;
        this.name = name;

        this.getPlayerInformation((useless) -> {
            if (this.uuid == null || this.name == null) {
                sender.sendMessage(ChatColor.RED + "Failed to find that player.");
            }
            else {
                this.attempt();
            }
        });
    }

    private void getPlayerInformation(Callback callback) {
        Player player = Bukkit.getPlayer(this.name);

        if (player != null) {
            this.uuid = player.getUniqueId();
            this.name = player.getName();

            callback.callback(null);
        }
        else {
            this.sender.sendMessage(ChatColor.GRAY + "(Resolving player information...)");

            Profile.getPlayerInformation(this.name, this.sender, (retrieved) -> {
                if (retrieved != null) {
                    uuid = UUIDType.fromString(retrieved.getAsJsonObject().get("uuid").getAsString());
                }

                callback.callback(null);
            });
        }
    }

    private void attempt() {
        new BukkitRunnable() {
            public void run() {
                Profile profile = Profile.getByUuid(uuid);

                if (profile.getAlts().size() == 0) {
                    profile.loadProfileAlts();
                }

                if (profile.getPunishments().size() == 0) {
                    sender.sendMessage(ChatColor.RED + "That player doesn't have any punishments.");

                    if(profile.getAlts().size() > 0) {

                        StringJoiner alts = new StringJoiner(", ");

                        for (UUID uuid : profile.getAlts()) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                            if (offlinePlayer != null && offlinePlayer.getName() != null) {
                                alts.add(offlinePlayer.getName());
                            }
                        }

                        sender.sendMessage(ChatColor.RED + "Maybe check with his alts? " + ChatColor.WHITE + alts.toString());
                    }
                    return;
                }

                StringJoiner messageJoiner = new StringJoiner("\n");
                int count = 1;

                sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + (profile.getName() == null ? name : profile.getName()) + " PUNISHMENTS");
                sender.sendMessage(" ");

                sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "BLACKLISTS");

                for(Punishment punishment : profile.getPunishmentsByType(PunishmentType.BLACKLIST)) {
                    String addedBy = punishment.getAddedBy() == null ? "CONSOLE" : Bukkit.getOfflinePlayer(punishment.getAddedBy()).getName();
                    String removedBy = punishment.getRemovedBy() == null ? "CONSOLE" : Bukkit.getOfflinePlayer(punishment.getRemovedBy()).getName();
                    String timeLeft = punishment.isRemoved()  ? "Removed by " + removedBy + " [" + (punishment.getRemovedReason() == null ? "Unknown" : punishment.getRemovedReason()) + "]" : punishment.getTimeLeft();
                    messageJoiner.add(ChatColor.GRAY + "[" + count + "] Reason: " + (punishment.getReason() == null ? "Unknown" : punishment.getReason()) + " by " + addedBy + "\n" + ChatColor.GRAY + "[-] Time Left: " + timeLeft);
                    count++;
                }

                if(messageJoiner.length() == 0) {
                    messageJoiner.add(ChatColor.GRAY + "None");
                }

                sender.sendMessage(messageJoiner.toString());

                messageJoiner = new StringJoiner("\n");
                count = 1;

                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "BANS");

                for(Punishment punishment : profile.getPunishmentsByType(PunishmentType.BAN)) {
                    String addedBy = punishment.getAddedBy() == null ? "CONSOLE" : Bukkit.getOfflinePlayer(punishment.getAddedBy()).getName();
                    String removedBy = punishment.getRemovedBy() == null ? "CONSOLE" : Bukkit.getOfflinePlayer(punishment.getRemovedBy()).getName();
                    String timeLeft = punishment.isRemoved()  ? "Removed by " + removedBy + " [" + (punishment.getRemovedReason() == null ? "Unknown" : punishment.getRemovedReason()) + "]" : punishment.getTimeLeft();
                    messageJoiner.add(ChatColor.GRAY + "[" + count + "] Reason: " + (punishment.getReason() == null ? "Unknown" : punishment.getReason()) + " by " + addedBy + "\n" + ChatColor.GRAY + "[-] Time Left: " + timeLeft);
                    count++;
                }

                if(messageJoiner.length() == 0) {
                    messageJoiner.add(ChatColor.GRAY + "None");
                }

                sender.sendMessage(messageJoiner.toString());

                messageJoiner = new StringJoiner("\n");
                count = 1;

                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "TEMP-BANS");

                for(Punishment punishment : profile.getPunishmentsByType(PunishmentType.TEMPBAN)) {
                    String addedBy = punishment.getAddedBy() == null ? "CONSOLE" : Bukkit.getOfflinePlayer(punishment.getAddedBy()).getName();
                    String removedBy = punishment.getRemovedBy() == null ? "CONSOLE" : Bukkit.getOfflinePlayer(punishment.getRemovedBy()).getName();
                    String timeLeft = punishment.isRemoved()  ? "Removed by " + removedBy + " [" + (punishment.getRemovedReason() == null ? "Unknown" : punishment.getRemovedReason()) + "]" : punishment.getTimeLeft();
                    messageJoiner.add(ChatColor.GRAY + "[" + count + "] Reason: " + (punishment.getReason() == null ? "Unknown" : punishment.getReason()) + " by " + addedBy + "\n" + ChatColor.GRAY + "[-] Time Left: " + timeLeft);
                    count++;
                }

                if(messageJoiner.length() == 0) {
                    messageJoiner.add(ChatColor.GRAY + "None");
                }

                sender.sendMessage(messageJoiner.toString());

                messageJoiner = new StringJoiner("\n");
                count = 1;

                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "MUTES");

                for(Punishment punishment : profile.getPunishmentsByType(PunishmentType.MUTE)) {
                    String addedBy = punishment.getAddedBy() == null ? "CONSOLE" : Bukkit.getOfflinePlayer(punishment.getAddedBy()).getName();
                    String removedBy = punishment.getRemovedBy() == null ? "CONSOLE" : Bukkit.getOfflinePlayer(punishment.getRemovedBy()).getName();
                    String timeLeft = punishment.isRemoved()  ?  "Removed by " + removedBy + " [" + (punishment.getRemovedReason() == null ? "Unknown" : punishment.getRemovedReason()) + "]" : punishment.getTimeLeft();
                    messageJoiner.add(ChatColor.GRAY + "[" + count + "] Reason: " + (punishment.getReason() == null ? "Unknown" : punishment.getReason()) + " by " + addedBy + "\n" + ChatColor.GRAY + "[-] Time Left: " + timeLeft);
                    count++;
                }

                if(messageJoiner.length() == 0) {
                    messageJoiner.add(ChatColor.GRAY + "None");
                }

                sender.sendMessage(messageJoiner.toString());

                messageJoiner = new StringJoiner("\n");
                count = 1;

                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "ALTS PUNISHMENTS");

                for (UUID altUUID : profile.getAlts()) {
                    if (!altUUID.equals(uuid)) {
                        Profile altProfile = Profile.getByUuid(altUUID);
                        String altName = Bukkit.getOfflinePlayer(altUUID) != null && Bukkit.getOfflinePlayer(altUUID).getName() != null ? "Unknown" : Bukkit.getOfflinePlayer(altUUID).getName();

                        if(altProfile.isBlacklisted()) {
                            messageJoiner.add(ChatColor.GRAY + "[" + count + "] " + altName + " is blacklisted. [More details? /history " + altName + "]");
                            count++;
                        }

                        if(altProfile.isBanned()) {
                            messageJoiner.add(ChatColor.GRAY + "[" + count + "] " + altName + " is banned. [More details? /history " + altName + "]");
                            count++;
                        }

                        if(altProfile.isMuted()) {
                            messageJoiner.add(ChatColor.GRAY + "[" + count + "] " + altName + " is muted. [More details? /history " + altName + "]");
                            count++;
                        }

                    }
                }

                if(messageJoiner.length() == 0) {
                    messageJoiner.add(ChatColor.GRAY + "None");
                }

                sender.sendMessage(messageJoiner.toString());

                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

}

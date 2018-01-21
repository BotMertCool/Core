package us.zonix.core.punishment.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.punishment.PunishmentHelper;
import us.zonix.core.punishment.PunishmentType;
import us.zonix.core.rank.Rank;
import us.zonix.core.util.Clickable;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;

import java.util.HashMap;
import java.util.UUID;

public class RequestCommand extends BaseCommand {

    private HashMap<UUID, Long> cooldown = new HashMap<>();

    @Command(name = "request", aliases = {"suggest", "bug"}, requiresPlayer = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /request [message]");
            return;
        }

        if(this.cooldown.containsKey(player.getUniqueId()) && this.cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
            player.sendMessage(ChatColor.RED + "Please wait, before sending a request again.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        String reason = sb.toString().trim();

        new BukkitRunnable() {
            public void run() {
                main.getRedisManager().writeRequest(player.getName(), reason);
                cooldown.put(player.getUniqueId(), System.currentTimeMillis() + 30 * 1000L);
                player.sendMessage(ChatColor.GREEN + "Your request has been submitted.");
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

}

package us.zonix.core.punishment.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.api.callback.AbstractBukkitCallback;
import us.zonix.core.api.request.PlayerRequest;
import us.zonix.core.profile.Profile;
import us.zonix.core.punishment.PunishmentHelper;
import us.zonix.core.punishment.PunishmentType;
import us.zonix.core.rank.Rank;
import us.zonix.core.util.Clickable;
import us.zonix.core.util.UUIDType;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;

import java.util.UUID;

public class StaffChatCommand extends BaseCommand {

    @Command(name = "staffchat", aliases = "sc", rank = Rank.TRIAL_MOD, requiresPlayer = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {

            if(this.main.getRedisManager().getStaffChat().contains(player.getUniqueId())) {
                this.main.getRedisManager().getStaffChat().remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "You have been removed from the staff chat.");
            } else {
                this.main.getRedisManager().getStaffChat().add(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "You have been added to the staff chat.");
            }

            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < (args.length); i++) {
            sb.append(args[i]).append(" ");
        }

        String message = sb.toString().trim();

        if(message.length() == 0) {
            player.sendMessage(ChatColor.RED + "Please type something.");
            return;
        }


        new BukkitRunnable() {
            public void run() {
                Profile profile = Profile.getByUuid(player.getUniqueId());

                if(profile != null) {
                    main.getRedisManager().writeStaffChat(player.getName(), profile.getRank(), message);
                }
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

}

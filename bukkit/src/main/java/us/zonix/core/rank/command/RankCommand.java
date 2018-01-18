package us.zonix.core.rank.command;

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
import us.zonix.core.rank.Rank;
import us.zonix.core.util.Clickable;
import us.zonix.core.util.UUIDType;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;

import java.util.UUID;

public class RankCommand extends BaseCommand {

    @Command(name = "rank", rank = Rank.MANAGER)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {

            Clickable clickable = new Clickable(ChatColor.RED + "Usage: /rank [target] [rank]", ChatColor.YELLOW + "Give a player a rank.", "");

            if(sender instanceof Player) {
                clickable.sendToPlayer((Player) sender);
            }

            return;
        }

        Rank rank;

        try {
            rank = Rank.valueOf(args[1].toUpperCase());
        }
        catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Could not parse that rank.");
            return;
        }

        new BukkitRunnable() {
            public void run() {
                Profile.getPlayerInformation(args[0], sender, new AbstractBukkitCallback() {
                    @Override
                    public void callback(JsonElement object) {
                        if (object == null) {
                            sender.sendMessage(ChatColor.RED + "Failed to find that player.");
                        }
                        else {
                            JsonObject o = object.getAsJsonObject();

                            UUID uuid = UUIDType.fromString(o.get("uuid").getAsString());
                            String name = o.get("name").getAsString();

                            main.getRequestProcessor().sendRequestAsync(new PlayerRequest.UpdateRankRequest(uuid, rank));
                            main.getRedisManager().writeRank(uuid, rank);

                            sender.sendMessage(ChatColor.GREEN + "Updated " + name + "'s rank to " + rank.getName() + ".");
                        }
                    }
                });
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

}

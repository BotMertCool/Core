package us.zonix.core.punishment.command;

import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.punishment.PunishmentHelper;
import us.zonix.core.punishment.PunishmentType;
import us.zonix.core.rank.Rank;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;

public class UnmuteCommand extends BaseCommand {

    @Command(name = "unmute", rank = Rank.MODERATOR)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            new FancyMessage(ChatColor.RED + "Usage: /unmute <target> [reason] [-s]").tooltip(ChatColor.YELLOW + "Remove a player's mute.").send(sender);
            return;
        }

        boolean silent = args[args.length - 1].equalsIgnoreCase("-s");

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < (silent ? args.length - 1 : args.length); i++) {
            sb.append(args[i]).append(" ");
        }

        String reason = sb.toString().trim();

        if (reason.equalsIgnoreCase("-s")) {
            sender.sendMessage(ChatColor.RED + "Please provide a valid reason.");
            return;
        }

        new BukkitRunnable() {
            public void run() {
                new PunishmentHelper(sender, args[0], PunishmentType.MUTE, reason, (long) -1, silent, true);
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

}

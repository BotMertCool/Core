package us.zonix.core.misc.command.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.punishment.AltsHelper;
import us.zonix.core.punishment.HistoryHelper;
import us.zonix.core.rank.Rank;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;

public class HistoryCommand extends BaseCommand {

    @Command(name = "history", rank = Rank.MODERATOR)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /history [player]");
            return;
        }

        new BukkitRunnable() {
            public void run() {
                new HistoryHelper(sender, args[0]);
            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }
}

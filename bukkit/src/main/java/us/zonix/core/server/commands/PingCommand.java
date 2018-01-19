package us.zonix.core.server.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.zonix.core.rank.Rank;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;

public class PingCommand extends BaseCommand {

    @Command(name = "ping", rank = Rank.DEFAULT)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (!(sender instanceof Player)) {
            return;
        }

        Player toCheck;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: /ping [player]");
                return;
            }
            toCheck = (Player) sender;
        } else {
            toCheck = Bukkit.getPlayer(StringUtils.join(args));
        }

        if (toCheck == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + toCheck.getName() + (toCheck.getName().endsWith("s") ? "'" : "'s") + ChatColor.GRAY + " current ping is " + ChatColor.GOLD + getPing(toCheck) + "ms" + ChatColor.GRAY + ".");
    }


    private int getPing(Player player) {
        int ping = ((CraftPlayer)player).getHandle().ping;

        if (ping >= 100) {
            return ping - 30;
        }

        if (ping >= 50) {
            return ping - 20;
        }

        if (ping >= 20) {
            return ping - 10;
        }

        return ping;
    }
}

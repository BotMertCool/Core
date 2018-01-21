package us.zonix.core.server.commands;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.zonix.core.rank.Rank;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;

public class SlowChatCommand extends BaseCommand {

	@Command(name = "slowchat", aliases = {"slow"}, rank = Rank.TRIAL_MOD)
	public void onCommand(CommandArgs command) {

		CommandSender sender = command.getSender();
		String[] args = command.getArgs();

		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: /slowchat [seconds]");
			return;
		}

		if(!NumberUtils.isNumber(args[0])) {
			sender.sendMessage(ChatColor.RED + "Invalid time.");
			return;
		}

		int time = Integer.parseInt(args[0]);
		this.main.getRedisManager().setChatSlowDownTime((long) time * 1000L);

		Bukkit.broadcastMessage(ChatColor.RED + (this.main.getRedisManager().getChatSlowDownTime() > 0 ? "Chat has been slowed by " + sender.getName() + " for " + time + "s." : "Slow Chat has been removed by " + sender.getName() + "."));
	}

}

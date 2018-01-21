package us.zonix.core.server.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.zonix.core.rank.Rank;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;

public class SilenceChatCommand extends BaseCommand {

	@Command(name = "silence", aliases = {"mutechat", "silencechat"}, rank = Rank.TRIAL_MOD)
	public void onCommand(CommandArgs command) {

		CommandSender sender = command.getSender();

		this.main.getRedisManager().setChatSilenced(!this.main.getRedisManager().isChatSilenced());

		Bukkit.broadcastMessage(ChatColor.RED + (this.main.getRedisManager().isChatSilenced() ? "Chat has been silenced by " + sender.getName() + "." : "Chat has been unsilenced by " + sender.getName() + "."));
	}

}

package us.zonix.core.server.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.zonix.core.CorePlugin;
import us.zonix.core.rank.Rank;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;

public class ClearChatCommand extends BaseCommand {

	@Command(name = "clearchat", aliases = {"cc"}, rank = Rank.TRIAL_MOD)
	public void onCommand(CommandArgs command) {

		CommandSender sender = command.getSender();

		this.main.getServer().getScheduler().runTaskAsynchronously(this.main, new Runnable() {
			@Override
			public void run() {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < 100; i++) {
					builder.append("§8 §8 §1 §3 §3 §7 §8 §r \n");
				}

				String message = builder.toString();

				for (Player player : CorePlugin.getInstance().getServer().getOnlinePlayers()) {
					player.sendMessage(message);
					player.sendMessage(ChatColor.RED + "Chat has been cleared by " + sender.getName() + ".");
				}
			}
		});
	}

}

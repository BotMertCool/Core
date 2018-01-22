package us.zonix.core.tasks;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.util.BungeeUtil;
import us.zonix.core.util.event.PreShutdownEvent;

@Getter
@Setter
public class ShutdownTask extends BukkitRunnable {

	private final static List<Integer> BROADCAST_TIMES = Arrays.asList(3600, 1800, 900, 600, 300, 180, 120, 60, 45, 30, 15, 10, 5, 4, 3, 2, 1);
	private static CorePlugin main = CorePlugin.getInstance();

	private int secondsUntilShutdown;

	@Override
	public void run() {
		if (ShutdownTask.BROADCAST_TIMES.contains(secondsUntilShutdown)) {
			main.getServer().broadcastMessage(ChatColor.RED + "The server will restart in " + ChatColor.RED + secondsUntilShutdown + ChatColor.RED + " seconds.");
		}

		if (this.secondsUntilShutdown <= 5) {
			main.getServer().getOnlinePlayers().forEach(player -> BungeeUtil.sendToServer(player, "hub"));
		}

		if (this.secondsUntilShutdown <= 0) {
			PreShutdownEvent event = new PreShutdownEvent();

			main.getServer().getPluginManager().callEvent(event);

			if (event.isCancelled()) {
				return;
			}

			main.getServer().shutdown();
		}

		this.secondsUntilShutdown--;
	}
}

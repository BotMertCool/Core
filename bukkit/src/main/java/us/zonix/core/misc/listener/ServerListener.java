package us.zonix.core.misc.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import us.zonix.core.CorePlugin;
import us.zonix.core.util.event.PreShutdownEvent;

public class ServerListener implements Listener {

	private final CorePlugin main = CorePlugin.getInstance();

	@EventHandler
	public void onServerCommand(ServerCommandEvent event) {
		String command = event.getCommand().replace("/", "");

		if (command == null) {
			return;
		}

		if (command.split(" ")[0].equalsIgnoreCase("stop")) {
			PreShutdownEvent shutdownEvent = new PreShutdownEvent();

			this.main.getServer().getPluginManager().callEvent(shutdownEvent);

			if (shutdownEvent.isCancelled()) {
				return;
			}

			this.handleShutdown();
		}
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (event.getPlayer().hasPermission("bukkit.command.stop")) {
			if (event.getMessage().replace("/", "").split(" ")[0].equalsIgnoreCase("stop")) {
				PreShutdownEvent shutdownEvent = new PreShutdownEvent();

				this.main.getServer().getPluginManager().callEvent(shutdownEvent);

				if (shutdownEvent.isCancelled()) {
					return;
				}

				this.handleShutdown();
			}
		}
	}

	@EventHandler(priority= EventPriority.LOWEST)
	public void onCommandProcess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();

		if (event.getMessage().toLowerCase().startsWith("//calc") || event.getMessage().toLowerCase().startsWith("//eval") || event.getMessage().toLowerCase().startsWith("//solve") || event.getMessage().toLowerCase().startsWith("/bukkit:") || event.getMessage().toLowerCase().startsWith("/me") || event.getMessage().toLowerCase().startsWith("/bukkit:me") || event.getMessage().toLowerCase().startsWith("/minecraft:") || event.getMessage().toLowerCase().startsWith("/minecraft:me")) {
			player.sendMessage(ChatColor.RED + "You cannot perform this command.");
			event.setCancelled(true);
		}
	}

	private void handleShutdown() {
		try {
			Thread.sleep(500L);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

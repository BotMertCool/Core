package us.zonix.core.server.tasks;

import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.server.ServerData;

@RequiredArgsConstructor
public class ServerHandlerTimeoutTask extends BukkitRunnable {

	private static final long TIME_OUT_DELAY = 15_000L;
	private final CorePlugin plugin;

	@Override
	public void run() {
		for (String serverName : this.plugin.getRedisManager().getServers().keySet()) {
			ServerData serverData = this.plugin.getRedisManager().getServers().get(serverName);

			if (serverData != null) {
				if (System.currentTimeMillis() - serverData.getLastUpdate() >= ServerHandlerTimeoutTask.TIME_OUT_DELAY) {
					this.plugin.getRedisManager().getServers().remove(serverName);
					this.plugin.getLogger().warning("The server \"" + serverName + "\" was removed due to it exceeding the timeout delay for heartbeats.");
				}
			}
		}
	}
}

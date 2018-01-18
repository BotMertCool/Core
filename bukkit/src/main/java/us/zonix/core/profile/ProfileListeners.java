package us.zonix.core.profile;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import us.zonix.core.CorePlugin;
import us.zonix.core.punishment.Punishment;
import us.zonix.core.punishment.PunishmentType;

public class ProfileListeners implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAysncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			return;
		}

		Profile profile = new Profile(event.getUniqueId());
		Punishment ban = profile.getBannedPunishment();

		profile.setLastLogin(System.currentTimeMillis());
		profile.setLastIp(event.getAddress().getHostAddress());

		if (profile.getName() == null) {
			profile.setName(event.getName());
			profile.save();
		}

		if (ban != null) {
			event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
			event.setKickMessage(ban.getType().getMessage());
			Profile.getProfiles().remove(profile);
			return;
		}

		if (profile.isBlacklisted()) {
			event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
			event.setKickMessage(PunishmentType.BLACKLIST.getMessage());
			Profile.getProfiles().remove(profile);
			return;
		}

		/*for (UUID uuid : profile.getAlts()) {
			if (!uuid.equals(event.getUniqueId())) {
				Profile alt = Profile.getByUuid(uuid);

				if (alt.isBlacklisted()) {
					event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
					event.setKickMessage("\n" + ChatColor.RED + "Your account has been blacklisted from the Exlode Network.\n" + ChatColor.RED + "This punishment is in relation to " + (alt.getName() == null ? "another account" : alt.getName()) + ".\n" + ChatColor.RED + "This punishment cannot be appealed.");
					Profile.getProfiles().remove(profile);
					return;
				}
			}
		}*/
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());

		if (profile != null) {
			CorePlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(CorePlugin.getInstance(), profile::save);
			Profile.getProfiles().remove(profile);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.getByUuid(player.getUniqueId());
		Punishment punishment = profile.getMutedPunishment();

		if (punishment != null) {
			event.setCancelled(true);
			player.sendMessage(PunishmentType.MUTE.getMessage().replace("%DURATION%", punishment.getTimeLeft()));
		}
	}

}

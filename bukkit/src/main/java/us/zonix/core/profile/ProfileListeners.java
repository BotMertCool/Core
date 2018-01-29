package us.zonix.core.profile;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import us.zonix.core.CorePlugin;
import us.zonix.core.api.request.MessageRequest;
import us.zonix.core.board.Board;
import us.zonix.core.punishment.Punishment;
import us.zonix.core.punishment.PunishmentType;
import us.zonix.core.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileListeners implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAysncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
			return;
		}

		Profile profile = new Profile(event.getUniqueId());
		Punishment ban = profile.getBannedPunishment();

		profile.setLastLogin(System.currentTimeMillis());
		profile.setIp(event.getAddress().getHostAddress());
		profile.setChatCooldown(0L);
		profile.setChatEnabled(true);

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

		if (profile.getAlts().size() == 0) {
			profile.loadProfileAlts();
		}

		for (UUID uuid : profile.getAlts()) {
			if (!uuid.equals(event.getUniqueId())) {
				Profile altProfile = Profile.getByUuid(uuid);
				Punishment bannedAlt = altProfile.getBannedPunishment();

				if (bannedAlt != null) {
					event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
					event.setKickMessage(bannedAlt.getType().getMessage());
					Profile.getProfiles().remove(altProfile);
					return;
				}

				if (altProfile.isBlacklisted()) {
					event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
					event.setKickMessage(PunishmentType.BLACKLIST.getMessage());
					Profile.getProfiles().remove(altProfile);
					return;
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerLogin(final PlayerLoginEvent event) {

		Profile profile = Profile.getByUuidIfAvailable(event.getPlayer().getUniqueId());

		if(event.getResult() == PlayerLoginEvent.Result.KICK_FULL && profile != null && profile.getRank().isAboveOrEqual(Rank.SILVER)) {
			event.allow();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {

		Player player = event.getPlayer();

		if (CorePlugin.getInstance().getBoardManager() != null) {
			CorePlugin.getInstance().getBoardManager().getPlayerBoards().put(player.getUniqueId(), new Board(player, CorePlugin.getInstance().getBoardManager().getAdapter()));
		}

		CorePlugin.getInstance().getServer().getScheduler().runTaskLater(CorePlugin.getInstance(), () -> {
			Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

			if (profile != null) {
				profile.updateTabList(profile.getRank());
			}
		}, 5L);

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());

		if (profile != null) {
			CorePlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(CorePlugin.getInstance(), profile::save);
			Profile.getProfiles().remove(profile);
		}

		if (CorePlugin.getInstance().getBoardManager() != null) {
			CorePlugin.getInstance().getBoardManager().getPlayerBoards().remove(player.getUniqueId());
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

		if (CorePlugin.getInstance().getRedisManager().getStaffChat().contains(player.getUniqueId())) {
			event.setCancelled(true);
			CorePlugin.getInstance().getRedisManager().writeStaffChat(player.getName(), profile.getRank(), ChatColor.stripColor(event.getMessage()));
		}

		List<Player> recipientList = new ArrayList<>(event.getRecipients());
		for (Player recipient : recipientList) {
			Profile profileRecipient = Profile.getByUuid(recipient.getUniqueId());
			if (profileRecipient != null) {

				if(!profileRecipient.isChatEnabled() || profileRecipient.getIgnored().contains(profile.getUuid())) {
					event.getRecipients().remove(recipient);
				}
			}
		}
	}

}

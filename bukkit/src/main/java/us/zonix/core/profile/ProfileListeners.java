package us.zonix.core.profile;

import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import us.zonix.core.CorePlugin;
import us.zonix.core.api.request.MessageRequest;
import us.zonix.core.api.request.PlayerRequest;
import us.zonix.core.board.Board;
import us.zonix.core.punishment.Punishment;
import us.zonix.core.punishment.PunishmentType;
import us.zonix.core.rank.Rank;
import us.zonix.core.util.auth.AuthenticationUtil;
import us.zonix.core.util.auth.PasswordGenerator;

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

		if(CorePlugin.getInstance().isHub() && profile.getTwoFactorAuthentication() != null && !profile.getIp().equalsIgnoreCase(event.getAddress().getHostAddress())) {
			profile.setAuthenticated(false);
		} else {
			profile.setAuthenticated(true);
		}

		Punishment ban = profile.getBannedPunishment();

		profile.setLastLogin(System.currentTimeMillis());

		if(profile.isAuthenticated()) {
			profile.setIp(event.getAddress().getHostAddress());
		}

		profile.setChatCooldown(0L);
		profile.setChatEnabled(true);

		if (profile.getName() == null) {
			profile.setName(event.getName());
			profile.save();
		}

		if (ban != null && !CorePlugin.getInstance().isHub()) {
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

				if (bannedAlt != null && !CorePlugin.getInstance().isHub()) {
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
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());

		if(profile != null && profile.getTwoFactorAuthentication() == null && profile.getRank().isAboveOrEqual(Rank.TRIAL_MOD)) {
			String token = PasswordGenerator.generateKey();
			String url = AuthenticationUtil.getTokenByKey(token);
			player.sendMessage("§8§m----------------------------------------------------");
			player.sendMessage(ChatColor.RED + "Download Authentication App - Google Auth, 1Password, Authy");
			player.sendMessage(ChatColor.RED + "Use Online App - https://gauth.apps.gbraad.nl/");
			player.sendMessage(ChatColor.RED + "QR code (Scan): " + url);
			player.sendMessage(ChatColor.RED + "or input the code in app: " + token);
			player.sendMessage(ChatColor.RED + "Finally, use /auth <token>");
			player.sendMessage("§8§m----------------------------------------------------");
			profile.setTwoFactorAuthentication(token);
			profile.setAuthenticated(false);
			CorePlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(CorePlugin.getInstance(), profile::save);
		}

		if(profile != null && profile.getTwoFactorAuthentication() != null && !profile.isAuthenticated() && profile.getRank().isAboveOrEqual(Rank.TRIAL_MOD)) {
			player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "AUTHENTICATE YOURSELF!");
			player.sendMessage(ChatColor.GRAY + "Usage: /auth <token>");
		}

		if (CorePlugin.getInstance().getBoardManager() != null) {
			CorePlugin.getInstance().getBoardManager().getPlayerBoards().put(player.getUniqueId(), new Board(player, CorePlugin.getInstance().getBoardManager().getAdapter()));
		}

		CorePlugin.getInstance().getServer().getScheduler().runTaskLater(CorePlugin.getInstance(), () -> {

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
			Profile.getProfiles().remove(profile.getUuid());
		}

		if (CorePlugin.getInstance().getBoardManager() != null) {
			CorePlugin.getInstance().getBoardManager().getPlayerBoards().remove(player.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onCommandEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (event.getMessage().toLowerCase().startsWith("/auth")) {
			return;
		}

		if(profile.getTwoFactorAuthentication() != null && !profile.isAuthenticated() && profile.getRank().isAboveOrEqual(Rank.TRIAL_MOD)) {
			player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "AUTHENTICATE YOURSELF!");
			player.sendMessage(ChatColor.GRAY + "Usage: /auth <token>");
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.getByUuid(player.getUniqueId());

		if(profile.getTwoFactorAuthentication() != null && !profile.isAuthenticated() && profile.getRank().isAboveOrEqual(Rank.TRIAL_MOD)) {
			event.setCancelled(true);
			return;
		}

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

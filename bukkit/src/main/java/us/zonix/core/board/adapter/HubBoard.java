package us.zonix.core.board.adapter;


import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.zonix.core.CorePlugin;
import us.zonix.core.board.Board;
import us.zonix.core.board.BoardAdapter;
import us.zonix.core.profile.Profile;
import us.zonix.core.punishment.Punishment;
import us.zonix.core.redis.queue.Queue;

public class HubBoard implements BoardAdapter {

	private final CorePlugin plugin = CorePlugin.getInstance();

	private int online;

	@Override
	public String getTitle(Player player) {
		return ChatColor.RED.toString() + ChatColor.BOLD + "Zonix Network";
	}

	@Override
	public void preLoop() {
		this.online = this.plugin.getRedisManager().getTotalPlayersOnline();
	}

	@Override
	public List<String> getScoreboard(Player player, Board board) {
		List<String> strings = new LinkedList<>();

		Profile profile = Profile.getByUuid(player.getUniqueId());

		if(profile != null && profile.getTwoFactorAuthentication() != null && !profile.isAuthenticated()) {
			strings.add(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------");
			strings.add(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "AUTHENTICATE");
			strings.add(ChatColor.GRAY.toString() + "Usage: /auth <token>");
			strings.add("   ");
			strings.add(ChatColor.RED.toString() + "Issues? Contact a Manager");
			strings.add(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------");
			return strings;
		}
		Punishment ban = profile.getBannedPunishment();

		if(ban != null && CorePlugin.getInstance().isHub()) {
			strings.add(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------");
			strings.add(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "BANNED");

			strings.add(ChatColor.GRAY.toString() + ban.getTimeLeft());
			strings.add("   ");
			strings.add(ChatColor.RED.toString() + "Appeal at www.zonix.us");
			strings.add(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------");
			return strings;
		}

		strings.add(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------");
		strings.add(ChatColor.GOLD.toString() + ChatColor.BOLD + "Online:");
		strings.add(ChatColor.WHITE.toString() + this.online);
		strings.add(" ");
		strings.add(ChatColor.GOLD.toString() + ChatColor.BOLD + "Rank:");
		strings.add(profile.getRank().getColor() + profile.getRank().getName());
		strings.add("  ");

		Queue queue = this.plugin.getQueueManager().getQueue(player);

		if(queue != null) {
			strings.add(ChatColor.GOLD.toString() + ChatColor.BOLD + "Queue:");
			strings.add(ChatColor.YELLOW.toString() + ChatColor.WHITE + queue.getServerName().replace("_", "-"));
			strings.add(ChatColor.YELLOW.toString() + ChatColor.WHITE + "Position: #" +  queue.getPosition(player) + " of " + queue.getPlayers().size());
			strings.add("  ");
		}

		strings.add(ChatColor.GOLD.toString() + "www.zonix.us");
		strings.add(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------");

		return strings;
	}

	@Override
	public void onScoreboardCreate(Player player, Scoreboard scoreboard) {
	}

	@Override
	public long getInterval() {
		return 20L;
	}

}

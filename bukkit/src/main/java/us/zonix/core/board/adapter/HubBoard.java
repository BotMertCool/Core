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

public class HubBoard implements BoardAdapter {

	private final CorePlugin plugin = CorePlugin.getInstance();

	private int online;

	@Override
	public String getTitle(Player player) {
		return ChatColor.RED.toString() + ChatColor.BOLD + "ZONIX";
	}

	@Override
	public void preLoop() {
		this.online = this.plugin.getRedisManager().getTotalPlayersOnline();
	}

	@Override
	public List<String> getScoreboard(Player player, Board board) {
		List<String> strings = new LinkedList<>();

		Profile profile = Profile.getByUuid(player.getUniqueId());

		strings.add(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------");
		strings.add(" ");
		strings.add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Online:");
		strings.add(ChatColor.WHITE.toString() + this.online);
		strings.add("  ");
		strings.add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Rank:");
		strings.add(ChatColor.WHITE.toString() + profile.getRank().getName());
		strings.add("   ");
		strings.add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Website:");
		strings.add("http://www.zonix.us");
		strings.add("    ");
		strings.add(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------");

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

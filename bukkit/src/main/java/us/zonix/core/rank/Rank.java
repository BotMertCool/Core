package us.zonix.core.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public enum Rank {

	DEFAULT("", ChatColor.GREEN.toString(), "Default", ""),
	SILVER(ChatColor.WHITE + "❖", ChatColor.GRAY.toString(), "Silver", ""),
	GOLD(ChatColor.YELLOW + "✯", ChatColor.GOLD.toString(), "Gold", ""),
	PLATINUM(ChatColor.AQUA + "❇", ChatColor.DARK_AQUA.toString(), "Platinum", ""),
	EMERALD(ChatColor.GREEN + "✵", ChatColor.DARK_GREEN.toString(), "Emerald", ""),
	ZONIX(ChatColor.GOLD + "❊", ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString(), "Zonix", ""),
	BUILDER(ChatColor.GRAY.toString() + "[", ChatColor.BLUE.toString(), "Builder", ChatColor.GRAY.toString() + "] "),
	MEDIA(ChatColor.GRAY.toString() + "[", ChatColor.LIGHT_PURPLE.toString() + ChatColor.ITALIC.toString(), "YouTuber", ChatColor.GRAY.toString() + "] "),
	FAMOUS(ChatColor.GRAY.toString() + "[", ChatColor.LIGHT_PURPLE.toString() + ChatColor.ITALIC.toString(), "Famous", ChatColor.GRAY.toString() + "] "),
	PARTNER(ChatColor.GRAY.toString() + "[", ChatColor.LIGHT_PURPLE.toString() + ChatColor.ITALIC.toString(), "Partner", ChatColor.GRAY.toString() + "] "),
	TRIAL_MOD(ChatColor.GRAY.toString() + "[", ChatColor.YELLOW.toString(), "Trial-Moderator", ChatColor.GRAY.toString() + "] "),
	MODERATOR(ChatColor.GRAY.toString() + "[", ChatColor.DARK_AQUA.toString(), "Moderator", ChatColor.GRAY.toString() + "] "),
	SENIOR_MODERATOR(ChatColor.GRAY.toString() + "[", ChatColor.DARK_PURPLE.toString(), "Sr. Moderator", ChatColor.GRAY.toString() + "] "),
	ADMINISTRATOR(ChatColor.GRAY.toString() + "[", ChatColor.RED.toString(), "Administrator", ChatColor.GRAY.toString() + "] "),
	MEDIA_OWNER(ChatColor.GRAY.toString() + "[", ChatColor.DARK_RED.toString(), "Owner", ChatColor.GRAY.toString() + "] "),
	MANAGER(ChatColor.GRAY.toString() + "[", ChatColor.LIGHT_PURPLE.toString() + ChatColor.ITALIC.toString(), "Manager", ChatColor.GRAY.toString() + "] "),
	DEVELOPER(ChatColor.GRAY.toString() + "[", ChatColor.AQUA.toString(), "Developer", ChatColor.GRAY.toString() + "] "),
	OWNER(ChatColor.GRAY.toString() + "[", ChatColor.DARK_RED.toString(), "Owner", ChatColor.GRAY.toString() + "] ");

	private final String prefix;
	private final String color;
	private final String name;
	private final String suffix;

	public boolean isAboveOrEqual(Rank rank) {
		return this.ordinal() >= rank.ordinal();
	}

	public static Rank getRankOrDefault(String rankName) {
		Rank rank;

		try {
			rank = Rank.valueOf(rankName.toUpperCase());
		}
		catch (Exception e) {
			rank = Rank.DEFAULT;
		}

		return rank;
	}

}

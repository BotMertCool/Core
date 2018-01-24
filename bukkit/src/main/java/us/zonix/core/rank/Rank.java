package us.zonix.core.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public enum Rank {

	DEFAULT("", ChatColor.GREEN.toString(), "Default", ""),
	SILVER("", ChatColor.GRAY.toString(), "Silver", ""),
	GOLD("", ChatColor.GOLD.toString(), "Gold", ""),
	PLATINUM("", ChatColor.DARK_AQUA.toString(), "Platinum", ""),
	EMERALD("", ChatColor.DARK_GREEN.toString(), "Emerald", ""),
	ZONIX("", ChatColor.RED.toString() + ChatColor.BOLD.toString(), "Zonix", ""),
	BUILDER(ChatColor.GRAY.toString() + "[", ChatColor.BLUE.toString(), "Builder", ChatColor.GRAY.toString() + "]"),
	MEDIA(ChatColor.GRAY.toString() + "[", ChatColor.LIGHT_PURPLE.toString(), "YouTuber", ChatColor.GRAY.toString() + "]"),
	FAMOUS(ChatColor.GRAY.toString() + "[", ChatColor.LIGHT_PURPLE.toString(), "Famous", ChatColor.GRAY.toString() + "]"),
	PARTNER(ChatColor.GRAY.toString() + "[", ChatColor.LIGHT_PURPLE.toString(), "Partner", ChatColor.GRAY.toString() + "]"),
	TRIAL_MOD(ChatColor.GRAY.toString() + "[", ChatColor.AQUA.toString() + ChatColor.ITALIC.toString(), "Trial-Mod", ChatColor.GRAY.toString() + "]"),
	MODERATOR(ChatColor.GRAY.toString() + "[", ChatColor.DARK_PURPLE.toString() + ChatColor.ITALIC.toString(), "Moderator", ChatColor.GRAY.toString() + "]"),
	ADMINISTRATOR(ChatColor.GRAY.toString() + "[", ChatColor.RED.toString() + ChatColor.ITALIC.toString(), "Administrator", ChatColor.GRAY.toString() + "]"),
	MEDIA_OWNER(ChatColor.GRAY.toString() + "[", ChatColor.DARK_RED.toString() + ChatColor.ITALIC.toString(), "Owner", ChatColor.GRAY.toString() + "]"),
	MANAGER(ChatColor.GRAY.toString() + "[", ChatColor.RED.toString() + ChatColor.ITALIC.toString(), "Manager", ChatColor.GRAY.toString() + "]"),
	DEVELOPER(ChatColor.GRAY.toString() + "[", ChatColor.AQUA.toString() + ChatColor.ITALIC.toString(), "Developer", ChatColor.GRAY.toString() + "]"),
	OWNER(ChatColor.GRAY.toString() + "[", ChatColor.DARK_RED.toString() + ChatColor.ITALIC.toString(), "Owner", ChatColor.GRAY.toString() + "]");

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

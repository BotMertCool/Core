package us.zonix.core.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public enum Rank {

	DEFAULT("", ChatColor.WHITE.toString(), "Default"),
	SILVER("", ChatColor.GRAY.toString(), "Silver"),
	GOLD("", ChatColor.GOLD.toString(), "Gold"),
	PLATINUM("", ChatColor.DARK_AQUA.toString(), "Platinum"),
	EMERALD("", ChatColor.DARK_GREEN.toString(), "Emerald"),
	ZONIX("", ChatColor.RED.toString() + ChatColor.BOLD.toString(), "Zonix"),
	BUILDER("", ChatColor.DARK_GREEN.toString(), "Builder"),
	MEDIA("", ChatColor.LIGHT_PURPLE.toString(), "YouTuber"),
	FAMOUS("", ChatColor.LIGHT_PURPLE.toString() + ChatColor.ITALIC.toString(), "Famous"),
	TRIAL_MOD("", ChatColor.AQUA.toString(), "Trial-Mod"),
	MODERATOR("", ChatColor.DARK_PURPLE.toString(), "Moderator"),
	ADMINISTRATOR("", ChatColor.RED.toString(), "Administrator"),
	MANAGER("", ChatColor.DARK_RED.toString(), "Head-Admin"),
	DEVELOPER("", ChatColor.AQUA.toString(), "Developer"),
	OWNER("", ChatColor.DARK_RED.toString(), "Owner");

	private final String prefix;
	private final String color;
	private final String name;

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

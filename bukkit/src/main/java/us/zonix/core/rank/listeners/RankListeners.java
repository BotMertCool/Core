package us.zonix.core.rank.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.zonix.core.profile.Profile;
import us.zonix.core.rank.Rank;

public class RankListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());

        event.setFormat("%1$s: %2$s");

        player.setDisplayName(player.getName());

        if (profile != null) {

            Rank rank = profile.getRank();

            String userTag = null;

            if(rank.isAboveOrEqual(Rank.TRIAL_MOD)) {
                userTag = rank.getPrefix() + rank.getColor() + ChatColor.ITALIC + rank.getName() + rank.getSuffix() + rank.getColor() + player.getName()  + ChatColor.WHITE;

                if(!player.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', userTag))) {
                    player.setDisplayName(ChatColor.translateAlternateColorCodes('&', userTag));
                }
            } else {
                userTag = rank.getPrefix() + rank.getColor() + rank.getSuffix() + rank.getColor() +  player.getName() + ChatColor.WHITE;

                if(!player.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', userTag))) {
                    player.setDisplayName(ChatColor.translateAlternateColorCodes('&', userTag));
                }
            }
        }
    }
}

package us.zonix.core.social;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.core.CorePlugin;
import us.zonix.core.api.request.MessageRequest;
import us.zonix.core.profile.Profile;
import us.zonix.core.rank.Rank;

public class SocialHelper {

    private static CorePlugin main = CorePlugin.getInstance();

    private String[] messages = new String[] {
            "We are currently having a 25% Sale on store.keapvp.com",
            "If you Butterfly click you will recieve a punishment you use DCPrevent",
            "Check out our Twitter for updates and giveaways! twitter.com/KeaPvP",
            "Staff applications are currrenty being accepted come into ts.keapvp.com ",
            "We have a TeamSpeak server! ts.keapvp.com",
            "See a hacker/cheater? Report them using /report",
            "Leaderboards will be up very soon!",
            "Have any problems come into ts.keapvp.com"};

    public void sendMessage(Player from, Profile fromProfile, Player to, Profile toProfile, String message) {
        //main.getRequestProcessor().sendRequestAsync(new MessageRequest.InsertRequest(from.getUniqueId(), "(To " + to.getName() + ") -> " + message));

        from.sendMessage(main.getConfigFile()
                .getString("messages.player_send")
                .replace("%TO%", (toProfile != null ? toProfile.getRank().getColor() : "") + to.getName())
                .replace("%MESSAGE%", ChatColor.stripColor(message)));

        to.sendMessage(main.getConfigFile()
                .getString("messages.player_receive")
                .replace("%FROM%", (fromProfile != null ? fromProfile.getRank().getColor() : "") + from.getName())
                .replace("%MESSAGE%", ChatColor.stripColor(message)));

        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());

            if (profile != null && profile.getRank().isAboveOrEqual(Rank.TRIAL_MOD) && profile.getOptions().isSocialSpy()) {
                player.sendMessage(main.getConfigFile()
                        .getString("messages.social_spy")
                        .replace("%FROM%", (fromProfile != null ? fromProfile.getRank().getColor() : "") + from.getName())
                        .replace("%TO%", (toProfile != null ? toProfile.getRank().getColor() : "") + to.getName())
                        .replace("%MESSAGE%", ChatColor.stripColor(message)));
            }
        }
    }
}

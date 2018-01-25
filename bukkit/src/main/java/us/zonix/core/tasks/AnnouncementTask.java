package us.zonix.core.tasks;

import org.bukkit.ChatColor;
import us.zonix.core.CorePlugin;

public class AnnouncementTask {

    private CorePlugin plugin;
    private String[] announcements;
    private int count;

    public AnnouncementTask(CorePlugin plugin) {
        this.plugin = plugin;
        this.count = 0;
        this.announcements = new String[] {
                "&7[&c*&7] &fCheck out our Twitter for updates and giveaways! &ctwitter.com/ZonixUS",
                "&7[&c*&7] &fWe are having a 50% OFF Sale in our store. &cstore.zonix.us",
                "&7[&c*&7] &fPremium Matches? You should get some at &cstore.zonix.us",
                "&7[&c*&7] &fSee a hacker/cheater? Report them using &c/report",
                "&7[&c*&7] &fStaff applications are &aopen &fapply at &cwww.zonix.us",
                "&7[&c*&7] &fWe have a TeamSpeak server. &cts.zonix.us",
                "&7[&c*&7] &fCheck out our leaderboards and see if you're on the top! &cwww.zonix.us/leaderboards"
        };

        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> plugin.getServer().broadcastMessage(getNextAnnouncerMessage()), 1200L, 1200L);
    }

    private String getNextAnnouncerMessage() {
        if (this.count >= this.announcements.length) {
            this.count = 0;
        }

        final String message = this.announcements[this.count];

        this.count++;

        return ChatColor.translateAlternateColorCodes('&', message);
    }

}

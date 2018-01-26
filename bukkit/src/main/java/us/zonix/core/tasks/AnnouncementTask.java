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
                "&8[&4✪&8] &7Check out our Twitter for updates and giveaways! &ctwitter.com/ZonixUS",
                "&8[&4✪&8] &7We are having a &c50% OFF &7sale in our store. &cstore.zonix.us",
                "&8[&4✪&8] &7Purchase premium matches on our store at &cstore.zonix.us",
                "&8[&4✪&8] &7Report cheating players by using &c/report&7.",
                //"&8[&4✪&8] &7Staff applications are &aopen &7apply at &cwww.zonix.us",
                "&8[&4✪&8] &7Join our Teamspeak server using: &cts.zonix.us",
                "&8[&4✪&8] &7Check out the leaderboards on our website &cwww.zonix.us/leaderboards"
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

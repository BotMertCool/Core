package us.zonix.core.tasks;

import org.bukkit.ChatColor;
import us.zonix.core.CorePlugin;
import us.zonix.core.util.CC;
import us.zonix.core.util.StringUtil;

public class AnnouncementTask {

    private CorePlugin plugin;
    private String[] announcements;
    private int count;

    public AnnouncementTask(CorePlugin plugin) {
        this.plugin = plugin;
        this.count = 0;
        this.announcements = new String[] {
                "&cDownload our free client-side anti-cheat:\n&4https://www.zonix.us/client/",
                "&cFollow our twitter for updates and giveaways:\n&4https://www.twitter.com/ZonixUS",
                "&cReport players that you think are breaking rules:\n&4/report <player> <reason>",
                "&cDonators get access to special abilities:\n&4/host, /symbols, /announce",
                "&cNeed support? Visit our support ticket system:\n&4https://www.zonix.us/support",
                "&cGrind out your statistics to the top:\n&4https://www.zonix.us/leaderboards/"
        };

        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            plugin.getServer().broadcastMessage(StringUtil.getBorderLine(CC.GRAY + CC.STRIKE_THROUGH));
            plugin.getServer().broadcastMessage(CC.BLANK_LINE);
            plugin.getServer().broadcastMessage(getNextAnnouncerMessage());
            plugin.getServer().broadcastMessage(CC.BLANK_LINE);
            plugin.getServer().broadcastMessage(StringUtil.getBorderLine(CC.GRAY + CC.STRIKE_THROUGH));
        }, 1200L, 1200L);
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

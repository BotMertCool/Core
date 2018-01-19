package us.zonix.core.util;

import org.bukkit.*;

public class LocationString
{
    private static final String splitter = ":";
    
    public static String toString(final Location location) {
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }
    
    public static Location fromString(final String location) {
        final String[] split = location.split(":");
        return new Location(Bukkit.getWorld(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
    }
}

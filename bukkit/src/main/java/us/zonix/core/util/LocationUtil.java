package us.zonix.core.util;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocationUtil {

    public static Location getHighestLocation(final Location origin) {
        return getHighestLocation(origin, null);
    }

    public static Location getHighestLocation(final Location origin, final Location def) {
        Preconditions.checkNotNull((Object)origin, (Object)"The location cannot be null");
        final Location cloned = origin.clone();
        final World world = cloned.getWorld();
        final int x = cloned.getBlockX();
        int y = world.getMaxHeight();
        final int z = cloned.getBlockZ();
        while (y > origin.getBlockY()) {
            final Block block = world.getBlockAt(x, --y, z);
            if (!block.isEmpty()) {
                final Location next = block.getLocation();
                next.setPitch(origin.getPitch());
                next.setYaw(origin.getYaw());
                return next;
            }
        }
        return def;
    }

    public static boolean isStuck(Player player) {
        Block block1 = player.getLocation().clone().getBlock();
        Block block2 = player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock();
        if(block1.getType().isSolid() && block2.getType().isSolid()) {
            return true;
        }
        if(block1.getRelative(BlockFace.DOWN).getType().isSolid() || block1.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()
                && block2.getRelative(BlockFace.DOWN).getType().isSolid() || block2.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
            return true;
        }
        return false;
    }

    public static void multiplyVelocity(Player player, Vector vector, double multiply, double addY) {
        vector.normalize();
        vector.multiply(multiply);
        vector.setY(vector.getY() + addY);
        player.setFallDistance(0.0F);

        player.setVelocity(vector);
    }
}

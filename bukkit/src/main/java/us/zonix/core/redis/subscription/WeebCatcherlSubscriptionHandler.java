package us.zonix.core.redis.subscription;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.CorePlugin;
import us.zonix.core.punishment.PunishmentType;
import us.zonix.core.punishment.helpers.PunishmentHelper;
import us.zonix.core.shared.redis.subscription.JedisSubscriptionHandler;

public class WeebCatcherlSubscriptionHandler implements JedisSubscriptionHandler<JsonObject> {

    @Override
    public void handleMessage(JsonObject object) {

        String message = object.get("message").getAsString();

        switch (message) {
            case "ban":
                String name = object.get("name").getAsString();
                String check = object.get("check").getAsString();

                new BukkitRunnable() {
                public void run() {
                    new PunishmentHelper(Bukkit.getConsoleSender(), name, PunishmentType.BAN, "Client: " + check, (long) -1, true, false);
                }}.runTaskAsynchronously(CorePlugin.getInstance());
                break;
        }
    }

}
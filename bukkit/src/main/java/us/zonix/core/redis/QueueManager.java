package us.zonix.core.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.*;
import redis.clients.jedis.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import us.zonix.core.CorePlugin;
import us.zonix.core.redis.queue.Queue;
import us.zonix.core.shared.redis.JedisPublisher;
import us.zonix.core.shared.redis.JedisSubscriber;
import us.zonix.core.shared.redis.subscription.JedisSubscriptionHandler;
import us.zonix.core.util.BungeeUtil;

public class QueueManager implements Listener {

    private final Map<String, Queue> queues;
    private final Gson gson;

    private CorePlugin plugin;
    private final AtomicBoolean serverOnline;

    private final JedisSubscriber<String> managerSubscriber;
    private final JedisPublisher<String> managerPublisher;

    private String[] availableQueues = new String[] {"practice-us", "practice-eu"};
    
    public QueueManager(CorePlugin plugin) {
        this.plugin = plugin;
        this.gson = new Gson();

        this.queues = new ConcurrentHashMap<>(10, 0.5f, 4);
        this.serverOnline = new AtomicBoolean();

        this.managerSubscriber = new JedisSubscriber<>(this.plugin.getJedisSettings(), "queuemanager" , String.class, new QueueManagerSubscriptionHandler());
        this.managerPublisher = new JedisPublisher<>(this.plugin.getJedisSettings(), "queuemanager");

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {

            for(String queue : this.availableQueues) {
                this.queues.put(queue.toLowerCase(), new Queue(plugin, queue));
                this.managerPublisher.writeDirectly("addServer`" + queue.replace("-", "_").toLowerCase());
                System.out.println("[Queue] Added " + queue.toLowerCase() + " server.");
            }
        });

    }
    
    public Queue getQueue(final String name) {
        return this.queues.get(name);
    }

    
    public boolean isServerOnline() {
        return this.serverOnline.get();
    }

    
    public Queue getQueue(final Player player) {
        for (final Queue queue : this.queues.values()) {
            if (queue.contains(player)) {
                return queue;
            }
        }
        return null;
    }

    private class QueueManagerSubscriptionHandler implements JedisSubscriptionHandler<String> {

        @Override
        public void handleMessage(String message) {

            if (message.equals("hello")) {
                serverOnline.set(true);

                for (final String server : queues.keySet()) {
                    managerPublisher.writeDirectly("addServer`" + server.toLowerCase().replace("-", "_"));
                }
            }
            else if (message.equals("goodbye")) {
                serverOnline.set(false);
                for (final Queue queue : queues.values()) {
                    queue.clear();
                }
            }

        }
    }
}

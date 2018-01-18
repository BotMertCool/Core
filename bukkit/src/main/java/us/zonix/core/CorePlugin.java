package us.zonix.core;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.api.CoreProcessor;
import us.zonix.core.profile.Profile;
import us.zonix.core.profile.ProfileListeners;
import us.zonix.core.punishment.command.*;
import us.zonix.core.rank.command.RankCommand;
import us.zonix.core.rank.listeners.RankListeners;
import us.zonix.core.redis.CoreRedisManager;
import us.zonix.core.shared.redis.JedisSettings;
import us.zonix.core.util.command.CommandFramework;
import us.zonix.core.util.file.ConfigFile;

@Getter
public class CorePlugin extends JavaPlugin {

	@Getter private static CorePlugin instance;

	private ConfigFile configFile;
	private CommandFramework commandFramework;

	private String apiUrl;
	private String apiKey;

	private String serverId;

	private JedisSettings jedisSettings;
	private CoreRedisManager redisManager;

	private CoreProcessor requestProcessor;

	@Override
	public void onEnable() {
		instance = this;

		this.configFile = new ConfigFile(this, "config");
		this.commandFramework = new CommandFramework(this);

		this.apiUrl = this.configFile.getString("api.url");
		this.apiKey = this.configFile.getString("api.key");

		this.serverId = this.configFile.getString("server.id");

		this.jedisSettings = new JedisSettings(
				this.configFile.getString("jedis.host"),
				this.configFile.getInt("jedis.port"),
				this.configFile.getConfiguration().contains("jedis.password") ? this.configFile.getString("jedis.password") : null
		);
		this.redisManager = new CoreRedisManager(this);

		this.requestProcessor = new CoreProcessor(this, this.apiUrl, this.apiKey);

		new BanCommand();
		new BlacklistCommand();
		new MuteCommand();
		new TempBanCommand();
		new UnbanCommand();
		new UnblacklistCommand();
		new UnmuteCommand();
		new RankCommand();

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.kickPlayer(ChatColor.RED + "This server is currently setting up...");
		}

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new ProfileListeners(), this);
		pm.registerEvents(new RankListeners(), this);

		// clean cached profiles every minute
		new BukkitRunnable() {
			public void run() {
				Profile.getProfiles().removeIf((profile -> Bukkit.getPlayer(profile.getUuid()) == null));
			}
		}.runTaskTimer(this, 0L, 20L * 60);
	}

}

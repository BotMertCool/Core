package us.zonix.core;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.api.CoreProcessor;
import us.zonix.core.board.BoardManager;
import us.zonix.core.board.adapter.HubBoard;
import us.zonix.core.misc.command.ReportCommand;
import us.zonix.core.misc.command.*;
import us.zonix.core.misc.command.game.*;
import us.zonix.core.misc.command.staff.AltsCommand;
import us.zonix.core.misc.command.staff.FreezeCommand;
import us.zonix.core.misc.command.staff.StaffChatCommand;
import us.zonix.core.misc.listener.HideStreamListener;
import us.zonix.core.misc.listener.ServerListener;
import us.zonix.core.profile.Profile;
import us.zonix.core.profile.ProfileListeners;
import us.zonix.core.punishment.command.*;
import us.zonix.core.rank.command.RankCommand;
import us.zonix.core.rank.listeners.RankListeners;
import us.zonix.core.redis.CoreRedisManager;
import us.zonix.core.redis.QueueManager;
import us.zonix.core.server.ServerManager;
import us.zonix.core.server.commands.*;
import us.zonix.core.server.listeners.ServerListeners;
import us.zonix.core.server.tasks.ServerHandlerTask;
import us.zonix.core.server.tasks.ServerHandlerTimeoutTask;
import us.zonix.core.social.SocialHelper;
import us.zonix.core.shared.redis.JedisSettings;
import us.zonix.core.social.command.MessageCommand;
import us.zonix.core.social.command.ReplyCommand;
import us.zonix.core.social.command.SocialSpyCommand;
import us.zonix.core.social.command.ToggleMessagesCommand;
import us.zonix.core.util.LocationString;
import us.zonix.core.util.command.CommandFramework;
import us.zonix.core.util.file.ConfigFile;
import us.zonix.core.util.inventory.UIListener;

import java.util.Iterator;
import java.util.UUID;

@Getter
public class CorePlugin extends JavaPlugin {

	@Getter private static CorePlugin instance;

	private ConfigFile configFile;
	private CommandFramework commandFramework;

	private String apiUrl;
	private String apiKey;

	private String serverId;
	private boolean hub;

	@Setter private Location spawnLocation;

	private JedisSettings jedisSettings;
	private CoreRedisManager redisManager;

	private BoardManager boardManager;
	private ServerManager serverManager;
	private QueueManager queueManager;

	private CoreProcessor requestProcessor;

	private SocialHelper socialHelper;

	@Override
	public void onEnable() {
		instance = this;

		this.configFile = new ConfigFile(this, "config");
		this.commandFramework = new CommandFramework(this);

		this.apiUrl = this.configFile.getString("api.url");
		this.apiKey = this.configFile.getString("api.key");

		this.serverId = this.configFile.getString("server.id");
		this.hub = this.configFile.getBoolean("server.hub");

		if (this.configFile.getConfiguration().contains("server.spawn")) {
			this.spawnLocation = LocationString.fromString(this.configFile.getString("server.spawn"));
		}

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		this.jedisSettings = new JedisSettings(
				this.configFile.getString("jedis.host"),
				this.configFile.getInt("jedis.port"),
				this.configFile.getConfiguration().contains("jedis.password") ? this.configFile.getString("jedis.password") : null
		);

		this.redisManager = new CoreRedisManager(this);

		this.requestProcessor = new CoreProcessor(this, this.apiUrl, this.apiKey);
		this.queueManager = new QueueManager(this);

		this.socialHelper = new SocialHelper();

		// punishment related
		new BanCommand();
		new BlacklistCommand();
		new MuteCommand();
		new TempBanCommand();
		new UnbanCommand();
		new UnblacklistCommand();
		new UnmuteCommand();
		new KickCommand();

		// staff related
		new RankCommand();
		new AltsCommand();
		new FreezeCommand();
		new SlowChatCommand();
		new ClearChatCommand();
		new SilenceChatCommand();
		new StaffChatCommand();
		new RequestCommand();
		new ReportCommand();

		// social related
		new MessageCommand();
		new ReplyCommand();
		new SocialSpyCommand();
		new ToggleMessagesCommand();

		// server related
		new SetMaxPlayersCommand();
		new PingCommand();
		new WhitelistCommand();
		new RegisterCommand();

		// game related
		new CraftCommand();
		new EnchantCommand();
		new FeedCommand();
		new GamemodeCommand();
		new HealCommand();
		new SpawnerCommand();
		new TeleportCommand();
		new TeleportHereCommand();
		new TopCommand();

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.kickPlayer(ChatColor.RED + "This server is currently setting up...");
		}

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new ProfileListeners(), this);
		pm.registerEvents(new RankListeners(), this);

		// misc
		pm.registerEvents(new HideStreamListener(), this);
		pm.registerEvents(new ServerListener(), this);

		if (this.hub) {
			this.serverManager = new ServerManager();

			pm.registerEvents(new UIListener(), this);
			pm.registerEvents(new ServerListeners(), this);

			new SetSpawnCommand();
			new JoinQueueCommand();
			new LeaveQueueCommand();

			this.setBoardManager(new BoardManager(new HubBoard()));
		}

		new ServerHandlerTask(this).runTaskTimerAsynchronously(this, 20L, 20L);
		new ServerHandlerTimeoutTask(this).runTaskTimerAsynchronously(this, 20L, 20L);

		// clean cached profiles every minute
		new BukkitRunnable() {
			public void run() {
				for (UUID uuid : Profile.getProfiles().keySet()) {
					if (Bukkit.getPlayer(uuid) == null) {
						Profile.getProfiles().remove(uuid);
					}
				}
			}
		}.runTaskTimerAsynchronously(this, 0L, 20L * 60);
	}

	@Override
	public void onDisable() {
		this.saveSpawnLocation();
	}

	public void setBoardManager(BoardManager boardManager) {
		this.boardManager = boardManager;

		long interval = this.boardManager.getAdapter().getInterval();

		this.getServer().getScheduler().runTaskTimerAsynchronously(this, this.boardManager, interval, interval);
	}


	private void saveSpawnLocation() {
		if (this.spawnLocation == null) {
			return;
		}

		try {
			this.configFile.getConfiguration().set("server.spawn", LocationString.toString(this.spawnLocation));
			this.configFile.getConfiguration().save(this.configFile.getFile());
		}
		catch (Exception ex) {}
	}

}

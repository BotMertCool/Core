package us.zonix.core;

import club.minemen.spigot.ClubSpigot;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.core.api.CoreProcessor;
import us.zonix.core.board.BoardManager;
import us.zonix.core.board.adapter.HubBoard;
import us.zonix.core.client.CosmeticCommand;
import us.zonix.core.misc.command.ReportCommand;
import us.zonix.core.misc.command.*;
import us.zonix.core.misc.command.game.*;
import us.zonix.core.misc.command.staff.*;
import us.zonix.core.misc.listener.HideStreamListener;
import us.zonix.core.misc.listener.ServerListener;
import us.zonix.core.misc.listener.StaffModeListener;
import us.zonix.core.profile.Profile;
import us.zonix.core.profile.ProfileListeners;
import us.zonix.core.punishment.command.*;
import us.zonix.core.rank.command.RankCommand;
import us.zonix.core.rank.listeners.RankListeners;
import us.zonix.core.redis.CoreRedisManager;
import us.zonix.core.redis.QueueManager;
import us.zonix.core.server.ServerManager;
import us.zonix.core.server.commands.*;
import us.zonix.core.server.handler.CustomMovementHandler;
import us.zonix.core.server.listeners.ServerListeners;
import us.zonix.core.server.tasks.ServerHandlerTask;
import us.zonix.core.server.tasks.ServerHandlerTimeoutTask;
import us.zonix.core.social.SocialHelper;
import us.zonix.core.shared.redis.JedisSettings;
import us.zonix.core.social.command.*;
import us.zonix.core.misc.staffmode.StaffModeManager;
import us.zonix.core.symbols.commands.PurchaseSymbolsCommand;
import us.zonix.core.symbols.commands.SymbolCommand;
import us.zonix.core.tab.TabList;
import us.zonix.core.tab.TabListManager;
import us.zonix.core.tasks.AnnouncementTask;
import us.zonix.core.tasks.AutomaticShutdownTask;
import us.zonix.core.tasks.ShutdownTask;
import us.zonix.core.util.LocationUtil;
import us.zonix.core.util.command.CommandFramework;
import us.zonix.core.util.file.ConfigFile;
import us.zonix.core.util.inventory.UIListener;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BooleanSupplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Getter
public class CorePlugin extends JavaPlugin {

	@Getter private static CorePlugin instance;

	private ConfigFile configFile;
	private CommandFramework commandFramework;

	private String apiUrl;
	private String apiKey;

	private String serverId;
	private boolean hub;
	@Setter private Long lastAnnounce;

	@Setter private Location spawnLocation;

	private JedisSettings jedisSettings;
	private CoreRedisManager redisManager;

	private BoardManager boardManager;
	private ServerManager serverManager;
	private QueueManager queueManager;
	private TabListManager tabListManager;
	private StaffModeManager staffModeManager;

	private CoreProcessor requestProcessor;

	private SocialHelper socialHelper;

	@Setter private ShutdownTask shutdownTask = null;

	@Setter private boolean joinable = false;
	@Setter private boolean setupMode = false;

	@Setter
	private BooleanSupplier setupSupplier = new BooleanSupplier() {
		private int attempts;

		@Override
		public boolean getAsBoolean() {
			return this.attempts++ >= 5;
		}
	};


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
			this.spawnLocation = LocationUtil.convertLocation(this.configFile.getString("server.spawn"));
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
		this.staffModeManager = new StaffModeManager();

		// client related
		new CosmeticCommand();

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
		new HistoryCommand();
		new StaffAuditCommand();
		new FreezeCommand();
		new SlowChatCommand();
		new ClearChatCommand();
		new SilenceChatCommand();
		new StaffChatCommand();
		new RequestCommand();
		new ReportCommand();
		new AuthCommand();
		new BuilderCommand();
		new StaffModeCommand();
		new InvseeCommand();
		new VanishCommand();

		// social related
		new MessageCommand();
		new ReplyCommand();
		new SocialSpyCommand();
		new ToggleMessagesCommand();
		new ToggleChatCommand();
		new IgnoreCommand();
		new SymbolCommand();
		new PurchaseSymbolsCommand();

		// server related
		new AnnounceCommand();
		new SetMaxPlayersCommand();
		new PingCommand();
		new WhitelistCommand();
		new RegisterCommand();

		// game related
		new AlertCommand();
		new CraftCommand();
		new EnchantCommand();
		new FeedCommand();
		new GamemodeCommand();
		new HealCommand();
		new SpawnerCommand();
		new TeleportCommand();
		new TeleportHereCommand();
		new TopCommand();
		new TeleportPositionCommand();
		new WorldCommand();

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new ProfileListeners(), this);
		pm.registerEvents(new RankListeners(), this);

		// misc
		pm.registerEvents(new HideStreamListener(), this);
		pm.registerEvents(new ServerListener(), this);
		pm.registerEvents(new UIListener(), this);
		pm.registerEvents(new StaffModeListener(), this);

		if (this.hub) {
			this.serverManager = new ServerManager();

			pm.registerEvents(new ServerListeners(), this);
			ClubSpigot.INSTANCE.addMovementHandler(new CustomMovementHandler());

			new SetSpawnCommand();
			new JoinQueueCommand();
			new LeaveQueueCommand();

			this.setBoardManager(new BoardManager(new HubBoard()));
		}

		new ServerHandlerTask(this).runTaskTimerAsynchronously(this, 20L, 20L);
		new ServerHandlerTimeoutTask(this).runTaskTimerAsynchronously(this, 20L, 20L);
		new AnnouncementTask(this);

		// clean cached profiles every minute
		new BukkitRunnable() {
			public void run() {
				Profile.getProfiles().keySet().removeIf(uuid -> Bukkit.getPlayer(uuid) == null);
			}
		}.runTaskTimerAsynchronously(this, 0L, 20L * 60);


		this.runSetupTimer();
		this.registerRestartTimer();

	}

	@Override
	public void onDisable() {
		this.saveSpawnLocation();
		doRestartFileCheck();

		for(Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (this.staffModeManager.hasStaffToggled(online)) {
				this.staffModeManager.toggleStaffMode(online);
			}
		}
	}

	public void setBoardManager(BoardManager boardManager) {
		this.boardManager = boardManager;

		long interval = this.boardManager.getAdapter().getInterval();

		this.getServer().getScheduler().runTaskTimerAsynchronously(this, this.boardManager, interval, interval);
	}

	public void useTabList() {
		if (this.tabListManager == null) {
			this.tabListManager = new TabListManager();
			this.tabListManager.setTabList(new TabList(this, this.tabListManager));
		} else {
			this.getLogger().warning("A plugin has already set the core to use the custom TabList");
		}
	}


	private void saveSpawnLocation() {
		if (this.spawnLocation == null) {
			return;
		}

		try {
			this.configFile.getConfiguration().set("server.spawn", LocationUtil.parseLocation(this.spawnLocation));
			this.configFile.getConfiguration().save(this.configFile.getFile());
		}
		catch (Exception ex) {}
	}

	private void doRestartFileCheck() {


		File uploadsFolder = new File(isHub() ? "../../uploads/hub" : "../uploads/practice").getAbsoluteFile();
		File pluginFolder = new File("./plugins").getAbsoluteFile();

		if(!uploadsFolder.exists() || !pluginFolder.exists()) {
			System.out.println("[Restart] Nothing to change, folder doesn't exist.");
			return;
		}

		if(uploadsFolder.isDirectory() && uploadsFolder.list().length == 0) {
			System.out.println("[Restart] There's no files to transfer.");
			return;
		}

		for(File file : uploadsFolder.listFiles()) {
			try {
				FileUtils.copyFileToDirectory(file, pluginFolder);
				System.out.println("[Restart] Copying " + file.getName() + " to plugin folder.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void runSetupTimer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (setupSupplier.getAsBoolean()) {
					CorePlugin.getInstance().setSetupMode(true);
					getLogger().info("The server is now setup and ready.");
					this.cancel();
				} else {
					getLogger().info("The server is currently setting up.");
				}
			}
		}.runTaskTimerAsynchronously(this, 0L, 20L);
	}

	private void registerRestartTimer() {

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 1);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		today.set(Calendar.AM_PM, Calendar.AM);

		Long currentTime = new Date().getTime();

		if (today.getTime().getTime() < currentTime) {
			today.add(Calendar.DATE, 1);
		}

		long startScheduler = today.getTime().getTime() - currentTime;

		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(new AutomaticShutdownTask(), startScheduler, MILLISECONDS);

	}


}

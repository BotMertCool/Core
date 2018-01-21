package us.zonix.core.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.zonix.core.CorePlugin;
import us.zonix.core.api.callback.AbstractBukkitCallback;
import us.zonix.core.api.request.PlayerRequest;
import us.zonix.core.api.request.PunishmentRequest;
import us.zonix.core.punishment.AltsHelper;
import us.zonix.core.punishment.Punishment;
import us.zonix.core.punishment.PunishmentType;
import us.zonix.core.rank.Rank;
import us.zonix.core.shared.api.callback.Callback;
import us.zonix.core.util.UUIDType;

@Getter
public class Profile {

	private static CorePlugin main = CorePlugin.getInstance();
	@Getter private static Set<Profile> profiles = new HashSet<>();

	private UUID uuid;
	@Setter private String name;
	@Setter private Long firstLogin;
	@Setter private Long lastLogin;
	@Setter private String ip;
	@Setter private Rank rank = Rank.DEFAULT;
	@Setter private UUID lastMessaged;
	@Setter private long lastRegister;
	@Setter private long chatCooldown;
	private List<Punishment> punishments;
	private Set<UUID> alts;
	private ProfileOptions options;

	public Profile(UUID uuid) {
		this.uuid = uuid;
		this.punishments = new ArrayList<>();
		this.alts = new HashSet<>();
		this.options = new ProfileOptions();

		this.loadProfile();
		this.loadPunishments();

		profiles.add(this);
	}

	public boolean isMuted() {
		for (Punishment punishment : punishments) {
			if (punishment.getType() == PunishmentType.MUTE) {
				if (punishment.isActive()) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isBanned() {
		for (Punishment punishment : punishments) {
			if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.TEMPBAN) {
				if (punishment.isActive()) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isBlacklisted() {
		for (Punishment punishment : punishments) {
			if (punishment.getType() == PunishmentType.BLACKLIST) {
				if (punishment.isActive()) {
					return true;
				}
			}
		}

		return false;
	}

	public Punishment getMutedPunishment() {
		for (Punishment punishment : punishments) {
			if (punishment.getType() == PunishmentType.MUTE) {
				if (punishment.isActive()) {
					return punishment;
				}
			}
		}

		return null;
	}

	public Punishment getBannedPunishment() {
		for (Punishment punishment : punishments) {
			if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.TEMPBAN) {
				if (punishment.isActive()) {
					return punishment;
				}
			}
		}

		return null;
	}

	public Punishment getBlacklistedPunishment() {
		for (Punishment punishment : punishments) {
			if (punishment.getType() == PunishmentType.BLACKLIST) {
				if (punishment.isActive()) {
					return punishment;
				}
			}
		}

		return null;
	}

	public List<Punishment> getPunishmentsByType(PunishmentType type) {
		List<Punishment> toReturn = new ArrayList<>();

		for (Punishment punishment : punishments) {
			if (punishment.getType().name().contains(type.name())) {
				toReturn.add(punishment);
			}
		}

		Map<Punishment, Long> toCompare = new HashMap<>();

		for (Punishment punishment : toReturn) {
			toCompare.put(punishment, punishment.getAddedAt());
		}

		toReturn.sort(new Comparator<Punishment>() {
			@Override
			public int compare(Punishment punishment, Punishment comparison) {
				return toCompare.get(comparison).compareTo(toCompare.get(punishment));
			}
		});

		return toReturn;
	}

	private void loadProfile() {
		JsonElement response = main.getRequestProcessor().sendRequest(new PlayerRequest.FetchByUuidRequest(this.uuid));

		if (response.isJsonNull() || response.isJsonPrimitive()) {
			System.out.println("Error while getting JSON response.");
			System.out.println("Issue: " + response.toString());
			return;
		}

		JsonObject data = response.getAsJsonObject();

		this.rank = data.get("rank") instanceof JsonNull ? Rank.DEFAULT : Rank.getRankOrDefault(data.get("rank").getAsString());
		this.firstLogin = data.get("firstLogin").getAsLong();

		if (this.lastLogin == null) {
			this.lastLogin = data.get("lastLogin").getAsLong();
		}

		this.ip = data.get("ip") instanceof JsonNull ? null : data.get("ip").getAsString();
	}

	public void loadProfileAlts() {

		JsonElement response = main.getRequestProcessor().sendRequest(new PlayerRequest.FetchAltsRequest(this.uuid));

		if (response.isJsonNull() || response.isJsonPrimitive()) {
			System.out.println("Error while getting JSON response.");
			System.out.println("Issue: " + response.toString());
			return;
		}

		JsonArray data = response.getAsJsonArray();

		data.iterator().forEachRemaining((altElement) -> {
			JsonObject element = altElement.getAsJsonObject();
			UUID profileUUID = UUID.fromString(element.get("uuid").getAsString());

			if(!this.uuid.toString().equalsIgnoreCase(profileUUID.toString())) {
				alts.add(profileUUID);
			}
		});
	}

	private void loadPunishments() {
		JsonElement response = main.getRequestProcessor().sendRequest(new PunishmentRequest.FetchByUuidRequest(this.uuid));

		if (response.isJsonNull() || response.isJsonPrimitive()) {
			System.out.println("Error while getting JSON response.");
			System.out.println("Issue: " + response.toString());
			return;
		}

		JsonArray data = response.getAsJsonArray();

		data.iterator().forEachRemaining((punishmentElement) -> {
			JsonObject punishmentObject = punishmentElement.getAsJsonObject();
			punishments.add(Punishment.fromJson(punishmentObject));
		});
	}

	public void save() {
		main.getRequestProcessor().sendRequest(new PlayerRequest.SaveRequest(this.uuid, this.name, this.lastLogin, main.getServerId(), this.ip));
	}

	public static void getPlayerInformation(String name, CommandSender sender, Callback callback) {
		Player player = Bukkit.getPlayer(name);

		if (player != null) {
			JsonObject retrieved = new JsonObject();
			retrieved.addProperty("uuid", player.getUniqueId().toString());
			retrieved.addProperty("name", player.getName());

			callback.callback(retrieved);
		}
		else {
			if (sender != null) {
				sender.sendMessage(ChatColor.GRAY + "(Resolving player information...)");
			}

			main.getRequestProcessor().sendRequestAsync(new PlayerRequest.FetchByNameRequest(name), new AbstractBukkitCallback() {
				@Override
				public void callback(JsonElement element) {
					if (element == null || element.isJsonNull() || element.isJsonPrimitive()) {
						callback.callback(null);
						return;
					}

					JsonObject data = element.getAsJsonObject();

					JsonObject object = new JsonObject();
					object.addProperty("uuid", data.get("uuid").getAsString());
					object.addProperty("name", data.get("name").getAsString());

					callback.callback(object);
				}

				@Override
				public void onError(String message) {
					callback.callback(null);
				}
			});
		}
	}

	public Player getPlayer() {
		return CorePlugin.getInstance().getServer().getPlayer(this.uuid);
	}

	public void updateTabList(Rank rank) {
		this.getPlayer().setPlayerListName(rank.getColor() + this.getPlayer().getName());
	}

	public static Profile getByUuidIfAvailable(UUID uuid) {
		for (Profile profile : profiles) {
			if (profile.getUuid().equals(uuid)) {
				return profile;
			}
		}

		return null;
	}

	public static Profile getByUuid(UUID uuid) {
        for (Profile profile : profiles) {
            if (profile.getUuid().equals(uuid)) {
                return profile;
            }
        }

        return new Profile(uuid);
	}

}

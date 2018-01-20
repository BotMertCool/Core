package us.zonix.core.api.request;

import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import us.zonix.core.rank.Rank;
import us.zonix.core.shared.api.request.Request;
import us.zonix.core.util.MapUtil;

@RequiredArgsConstructor
public abstract class PlayerRequest implements Request {

	private final String path;

	@Override public String getPath() {
		return "/player/" + this.path;
	}

	@Override public Map<String, Object> toMap() {
		return null;
	}

	public static final class FetchByUuidRequest extends PlayerRequest {

		public FetchByUuidRequest(UUID uuid) {
			super("fetch_by_uuid/" + uuid.toString());
		}

	}

	public static final class FetchByNameRequest extends PlayerRequest {

		public FetchByNameRequest(String name) {
			super("fetch_by_name/" + name);
		}

	}

	public static final class FetchAltsRequest extends PlayerRequest {

		public FetchAltsRequest(UUID uuid) {
			super("fetch_by_ip/" + uuid.toString());
		}

	}

	public static final class SaveRequest extends PlayerRequest {

		private final UUID uuid;
		private final String name;
		private final Long lastLogin;
		private final String ip;

		public SaveRequest(UUID uuid, String name, Long lastLogin, String ip) {
			super("save");

			this.uuid = uuid;
			this.name = name;
			this.lastLogin = lastLogin;
			this.ip = ip;
		}

		@Override
		public Map<String, Object> toMap() {
			JsonObject data = new JsonObject();
			data.addProperty("uuid", this.uuid.toString());
			data.addProperty("name", this.name);
			data.addProperty("last_login", this.lastLogin);
			data.addProperty("ip", this.ip);

			return MapUtil.of(
					"data", data
			);
		}

	}

	public static final class UpdateRankRequest extends PlayerRequest {

		private final UUID uuid;
		private final Rank rank;

		public UpdateRankRequest(UUID uuid, Rank rank) {
			super("update-rank");

			this.uuid = uuid;
			this.rank = rank;
		}

		@Override
		public Map<String, Object> toMap() {
			return MapUtil.of(
					"uuid", this.uuid.toString(),
					"rank", this.rank.name()
			);
		}
	}

}

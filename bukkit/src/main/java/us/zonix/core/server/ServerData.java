package us.zonix.core.server;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @since 8/29/2017
 */
@Getter
@Setter
public class ServerData {

	private String serverName;
	private int onlinePlayers;
	private int maxPlayers;
	private long lastUpdate;
	private boolean whitelisted;

}

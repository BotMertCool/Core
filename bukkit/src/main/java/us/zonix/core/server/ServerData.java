package us.zonix.core.server;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerData {

	private String serverName;
	private int onlinePlayers;
	private int maxPlayers;
	private long lastUpdate;
	private boolean whitelisted;

}

package fr.prodrivers.bukkit.bedrockbridger.session;

import fr.prodrivers.bukkit.bedrockbridger.Log;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.geysermc.common.window.FormWindow;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.session.auth.BedrockClientData;
import org.geysermc.connector.network.session.cache.WindowCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@EqualsAndHashCode
public class BedrockSession {
	private final static Map<UUID, BedrockSession> sessions = new HashMap<>();

	private final GeyserSession realSession;

	@Getter private final UUID UUID;
	@Getter private final String xboxUUID;
	@Getter private final String xboxName;
	@Getter private final String username;
	@Getter private final BedrockClientData.UIProfile uiProfile;
	@Getter private final BedrockClientData.InputMode inputMode;
	@Getter private final WindowCache windowCache;

	private BedrockSession(GeyserSession realSession) {
		this.realSession = realSession;
		this.UUID = this.realSession.getPlayerEntity().getUuid();
		this.xboxUUID = this.realSession.getAuthData().getXboxUUID();
		this.xboxName = this.realSession.getAuthData().getName();
		BedrockClientData clientData = this.realSession.getClientData();
		this.username = clientData.getUsername();
		this.uiProfile = clientData.getUiProfile();
		this.inputMode = clientData.getCurrentInputMode();
		this.windowCache = this.realSession.getWindowCache();
	}

	public void sendForm(FormWindow window) {
		this.realSession.sendForm(window);
	}

	public void sendForm(FormWindow window, int id) {
		this.realSession.sendForm(window, id);
	}

	public int numberOfForms() {
		return getWindowCache().getWindows().size();
	}

	public static boolean hasSession(Player player) {
		if(sessions.containsKey(player.getUniqueId())) {
			return true;
		}
		return getSession(player).isPresent();
	}

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public static BedrockSession of(GeyserSession geyserSession) {
		return getSession(geyserSession.getPlayerEntity().getUuid()).get();
	}

	public static Optional<BedrockSession> getSession(Player player) {
		return getSession(player.getUniqueId());
	}

	public static Optional<BedrockSession> getSession(UUID uuid) {
		BedrockSession session = sessions.get(uuid);
		if(session != null) {
			if(!session.realSession.getUpstream().isClosed()) {
				return Optional.of(session);
			}
		}

		try {
			Class.forName("org.geysermc.connector.network.session.GeyserSession");
			Optional<GeyserSession> geyserSession = getGeyserSession(uuid);
			return geyserSession.map(BedrockSession::fromGeyserSession);
		} catch(ClassNotFoundException e) {
			Log.warning("No local Geyser connector detected.");
		}
		return Optional.empty();
	}

	public static void dropSessionFromCache(Player player) {
		sessions.remove(player.getUniqueId());
	}

	public static void dropSessionFromCache(UUID uuid) {
		sessions.remove(uuid);
	}

	private static BedrockSession fromGeyserSession(GeyserSession geyserSession) {
		BedrockSession session = new BedrockSession(geyserSession);
		sessions.put(session.getUUID(), session);
		return session;
	}

	private static Optional<GeyserSession> getGeyserSession(Player player) {
		return getGeyserSession(player.getUniqueId());
	}

	private static Optional<GeyserSession> getGeyserSession(UUID uuid) {
		try {
			// Test if Geyser is in classpath
			Class.forName("org.geysermc.connector.GeyserConnector");
			return GeyserConnector.getInstance().getPlayers().stream()
					.filter(session -> session.getPlayerEntity().getUuid().equals(uuid))
					.findFirst();
		} catch(ClassNotFoundException e) {
			Log.warning("No local Geyser connector detected.");
		}
		return Optional.empty();
	}
}

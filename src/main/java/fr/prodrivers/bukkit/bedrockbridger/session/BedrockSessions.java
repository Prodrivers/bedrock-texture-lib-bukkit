package fr.prodrivers.bukkit.bedrockbridger.session;

import fr.prodrivers.bukkit.bedrockbridger.Log;
import org.bukkit.entity.Player;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.Optional;

public class BedrockSessions {
	public static boolean hasSession(Player player) {
		return getSession(player).isPresent();
	}

	public static Optional<GeyserSession> getSession(Player player) {
		try {
			// Test if Geyser is in classpath
			Class.forName("org.geysermc.connector.GeyserConnector");
			return GeyserConnector.getInstance().getPlayers().stream()
					.filter(session -> session.getPlayerEntity().getUuid().equals(player.getUniqueId()))
					.findFirst();
		} catch(ClassNotFoundException e) {
			Log.warning("No local Geyser connector detected.");
			return Optional.empty();
		}
	}
}

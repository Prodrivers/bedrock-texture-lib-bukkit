package fr.prodrivers.bukkit.bedrockbridger.event;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import fr.prodrivers.bukkit.bedrockbridger.BedrockBridge;
import fr.prodrivers.bukkit.bedrockbridger.Log;
import fr.prodrivers.bukkit.bedrockbridger.event.events.packet.UpstreamPacketReceiveEvent;
import fr.prodrivers.bukkit.bedrockbridger.session.BedrockSession;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.GeyserEvent;
import org.geysermc.connector.event.handlers.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class BedrockEventManager {
	private final Map<Class<? extends BedrockPacket>, EventHandler<? extends GeyserEvent>> handlers = new HashMap<>();

	/**
	 * Make the plugin listen to upstream receive events for a specific packet.
	 *
	 * @param packetClass Class of packet which events we should listen to
	 * @return {@code true} if listener was successfully initialized
	 */
	public boolean listenUpstreamReceive(Class<? extends BedrockPacket> packetClass) {
		if(handlers.containsKey(packetClass)) {
			return true;
		}

		try {
			Class.forName("org.geysermc.connector.GeyserConnector");

			Class<? extends GeyserEvent> upstreamClass = upstreamReceiveEventClassOf(packetClass);

			EventHandler<? extends GeyserEvent> handler = GeyserConnector.getInstance().getEventManager()
					.on(upstreamClass, this::onUpstreamReceiveEvent)
					.priority(30)
					.ignoreCancelled(false);

			Log.info("Registered listener " + upstreamClass + " for packet " + packetClass.getSimpleName());

			handlers.put(packetClass, handler);

			return true;
		} catch(ClassNotFoundException e) {
			Log.warning("No local Geyser connector detected.");
		}
		return false;
	}

	/**
	 * Make the plugin stop listening to upstream receive events for a specific packet.
	 *
	 * @param packetClass Class of packet which events we should listen to
	 * @return {@code true} if listener was successfully initialized
	 */
	public boolean unlistenUpstreamReceive(Class<? extends BedrockPacket> packetClass) {
		if(!handlers.containsKey(packetClass)) {
			return false;
		}

		try {
			Class.forName("org.geysermc.connector.GeyserConnector");

			GeyserConnector.getInstance().getEventManager().unregister(handlers.get(packetClass));

			return true;
		} catch(ClassNotFoundException e) {
			Log.warning("No local Geyser connector detected.");
		}
		return false;
	}

	private Class<? extends GeyserEvent> upstreamReceiveEventClassOf(Class<? extends BedrockPacket> packetClass) {
		try {
			return (Class<? extends GeyserEvent>) Class.forName(String.format("org.geysermc.connector.event.events.packet.upstream.%sReceive", packetClass.getSimpleName()));
		} catch(ClassNotFoundException e) {
			Log.severe("Missing event for packet: " + packetClass);
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	private void onUpstreamReceiveEvent(final GeyserEvent inObject) {
		new Thread(() -> {
			org.geysermc.connector.event.events.packet.UpstreamPacketReceiveEvent in = (org.geysermc.connector.event.events.packet.UpstreamPacketReceiveEvent) inObject;
			UpstreamPacketReceiveEvent<? extends BedrockPacket> bukkitEvent = new UpstreamPacketReceiveEvent<>(BedrockSession.of(in.getSession()), in.getPacket());
			Log.info("Received packet " + in.getPacket().getClass().getSimpleName() + " from upstream.");
			bukkitEvent.setCancelled(in.isCancelled());
			BedrockBridge.getInstance().getServer().getPluginManager().callEvent(bukkitEvent);
			Log.info("Event was called.");
		}).start();
	}
}

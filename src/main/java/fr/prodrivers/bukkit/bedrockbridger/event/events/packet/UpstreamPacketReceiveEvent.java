package fr.prodrivers.bukkit.bedrockbridger.event.events.packet;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.Cancellable;
import org.geysermc.connector.event.GeyserEvent;
import org.geysermc.connector.event.Session;
import org.geysermc.connector.network.session.GeyserSession;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Triggered each time a packet is received from the Downstream server.
 *
 * If cancelled then regular processes of the packet will not proceed
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("JavaDoc")
public class UpstreamPacketReceiveEvent<T extends BedrockPacket> extends GeyserEvent implements Cancellable, Session {
	@NonNull
	private final GeyserSession session;

	/**
	 * Upstream packet
	 *
	 * @param packet set the upstream packet
	 * @return get the current upstream packet
	 */
	@NonNull
	private T packet;

	/**
	 * Upstream packet
	 *
	 * @param packet set the upstream packet
	 * @return get the current upstream packet
	 */
	@NonNull
	private org.geysermc.connector.event.events.packet.UpstreamPacketReceiveEvent<T> realEvent;

	@Override
	public boolean isCancelled() {
		return realEvent.isCancelled();
	}

	@Override
	public void setCancelled(boolean cancel) {
		realEvent.setCancelled(true);
	}
}
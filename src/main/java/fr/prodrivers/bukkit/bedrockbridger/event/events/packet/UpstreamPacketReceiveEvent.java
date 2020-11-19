package fr.prodrivers.bukkit.bedrockbridger.event.events.packet;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import fr.prodrivers.bukkit.bedrockbridger.session.BedrockSession;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Triggered each time a packet is received from the Downstream server.
 *
 * If cancelled then regular processes of the packet will not proceed
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("JavaDoc")
public class UpstreamPacketReceiveEvent<T extends BedrockPacket> extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	@NonNull
	private final BedrockSession session;

	/**
	 * Upstream packet
	 *
	 * @param packet set the upstream packet
	 * @return get the current upstream packet
	 */
	@NonNull
	private T packet;

	public UpstreamPacketReceiveEvent(BedrockSession session, T packet) {
		super(true);
		this.session = session;
		this.packet = packet;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
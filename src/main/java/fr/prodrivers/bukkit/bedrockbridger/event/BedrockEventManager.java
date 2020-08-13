package fr.prodrivers.bukkit.bedrockbridger.event;

import fr.prodrivers.bukkit.bedrockbridger.Log;
import fr.prodrivers.bukkit.bedrockbridger.event.annotations.GeyserEventHandler;
import fr.prodrivers.bukkit.bedrockbridger.event.events.packet.UpstreamPacketReceiveEvent;
import fr.prodrivers.bukkit.bedrockbridger.event.handlers.MethodEventHandler;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.GeyserEvent;
import org.geysermc.connector.event.handlers.EventHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BedrockEventManager {
	private final Map<Object, ArrayList<Object>> classEventHandlers = new HashMap<>();

	/**
	 * Register all Events contained in an instantiated class. The methods must be annotated by @Event
	 */
	public boolean registerEvents(Object listener) {
		try {
			Class.forName("org.geysermc.connector.GeyserConnector");
			List<org.geysermc.connector.event.handlers.EventHandler<?>> handlers = new ArrayList<>();
			for(Method method : listener.getClass().getMethods()) {
				// Check that the method is annotated with @Event
				if(method.getAnnotation(GeyserEventHandler.class) == null) {
					continue;
				}

				// Make sure it only has a single Event parameter
				if(method.getParameterCount() != 1 || !GeyserEvent.class.isAssignableFrom(method.getParameters()[0].getType())) {
					Log.severe("Cannot register EventHandler as its only parameter must be an Event: " + listener.getClass().getSimpleName() + "#" + method.getName());
					continue;
				}

				EventHandler<?> handler = new MethodEventHandler<>(GeyserConnector.getInstance().getEventManager(), listener, method);
				GeyserConnector.getInstance().getEventManager().register(handler);
				handlers.add(handler);
			}

			if(!classEventHandlers.containsKey(listener.getClass())) {
				classEventHandlers.put(listener.getClass(), new ArrayList<>());
			}
			classEventHandlers.get(listener.getClass()).addAll(handlers);
			return true;
		} catch(ClassNotFoundException e) {
			Log.warning("No local Geyser connector detected.");
		}
		return false;
	}

	/**
	 * Unregister all events in class
	 */
	public boolean unregisterEvents(Object listener) {
		try {
			Class.forName("org.geysermc.connector.GeyserConnector");
			if(!classEventHandlers.containsKey(listener)) {
				return false;
			}

			for(Object handler : classEventHandlers.get(listener)) {
				GeyserConnector.getInstance().getEventManager().unregister((EventHandler<?>) handler);
			}

			classEventHandlers.remove(listener);
			return true;
		} catch(ClassNotFoundException e) {
			Log.warning("No local Geyser connector detected.");
		}
		return false;
	}

	public static Class<?> convertToRealEventClass(Class<?> clss, Class<?> packetClass) {
		if(clss == UpstreamPacketReceiveEvent.class) {
			try {
				return Class.forName(String.format("org.geysermc.connector.event.events.packet.upstream.%sReceive", packetClass.getSimpleName()));
			} catch (ClassNotFoundException e) {
				Log.severe("Missing event for packet: " + packetClass);
				return clss;
			}
		}
		return clss;
	}

	@SuppressWarnings("rawtypes")
	public static GeyserEvent convertToRealEventObject(GeyserEvent inObject) {
		if(org.geysermc.connector.event.events.packet.UpstreamPacketReceiveEvent.class.isAssignableFrom(inObject.getClass())) {
			org.geysermc.connector.event.events.packet.UpstreamPacketReceiveEvent in = (org.geysermc.connector.event.events.packet.UpstreamPacketReceiveEvent) inObject;
			return new UpstreamPacketReceiveEvent<>(in.getSession(), in.getPacket(), in);
		}
		return inObject;
	}
}

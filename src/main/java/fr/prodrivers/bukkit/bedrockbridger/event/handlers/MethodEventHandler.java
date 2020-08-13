package fr.prodrivers.bukkit.bedrockbridger.event.handlers;

import fr.prodrivers.bukkit.bedrockbridger.event.BedrockEventManager;
import fr.prodrivers.bukkit.bedrockbridger.event.annotations.GeyserEventHandler;
import lombok.Getter;
import org.geysermc.connector.event.Cancellable;
import org.geysermc.connector.event.EventManager;
import org.geysermc.connector.event.GeyserEvent;
import org.geysermc.connector.event.handlers.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Provides an event handler for an annotated method
 */
@Getter
public class MethodEventHandler<T extends GeyserEvent> extends EventHandler<T> {
	private final Object handlerClass;
	private final Method method;
	private final int priority;
	private final boolean ignoreCancelled;

	@SuppressWarnings("unchecked")
	public MethodEventHandler(EventManager manager, Object handlerClass, Method method) {
		super(manager, (Class<T>) BedrockEventManager.convertToRealEventClass(method.getParameters()[0].getType(), method.getAnnotation(GeyserEventHandler.class).packet()));

		GeyserEventHandler annotation = method.getAnnotation(GeyserEventHandler.class);
		this.handlerClass = handlerClass;
		this.method = method;
		this.priority = annotation.priority();
		this.ignoreCancelled = annotation.ignoreCancelled();
	}

	@Override
	public void execute(T event) throws EventHandlerException {
		if (event instanceof Cancellable) {
			if (((Cancellable) event).isCancelled() && !isIgnoreCancelled()) {
				return;
			}
		}

		try {
			GeyserEvent realEvent = BedrockEventManager.convertToRealEventObject(event);
			method.invoke(handlerClass, realEvent);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new EventHandlerException("Unable to execute Event Handler", e);
		}
	}
}
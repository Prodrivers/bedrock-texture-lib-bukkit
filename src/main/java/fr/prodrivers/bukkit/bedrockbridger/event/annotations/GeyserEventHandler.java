package fr.prodrivers.bukkit.bedrockbridger.event.annotations;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import fr.prodrivers.bukkit.bedrockbridger.event.EventHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Event Methods must be decorated with this annotation to receive events.
 * <p>
 * The class the method belongs to must also be registered with the plugin manager
 * unless it is already decorated with the @Plugin annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface GeyserEventHandler {
	// Events are processed from lowest to highest priority
	int priority() default EventHandler.PRIORITY.NORMAL;

	// If ignoreCancelled is true then the handler will not be executed
	boolean ignoreCancelled() default false;

	// Filters if applicable
	Class<?> packet() default BedrockPacket.class;
}

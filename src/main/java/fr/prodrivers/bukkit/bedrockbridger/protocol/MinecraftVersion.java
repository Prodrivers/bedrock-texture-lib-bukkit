package fr.prodrivers.bukkit.bedrockbridger.protocol;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum MinecraftVersion {
	V_1_16_100(409, "1.16.100"),
	V_1_16_2(409, "1.16.2"),
	V_1_16_1(407, "1.16.0"),
	V1_14(390, "1.14.0");

	@Getter
	private final int protocolVersion;
	@Getter
	private final String versionTag;

	private static final Map<Integer, MinecraftVersion> protocolMap = new HashMap<>();

	static {
		for(MinecraftVersion value : MinecraftVersion.values()) {
			protocolMap.put(value.getProtocolVersion(), value);
		}
	}

	public static MinecraftVersion fromProtocol(int protocolVersion) {
		return protocolMap.get(protocolVersion);
	}

	MinecraftVersion(final int protocolVersion, final String versionTag) {
		this.protocolVersion = protocolVersion;
		this.versionTag = versionTag;
	}
}

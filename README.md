# BedrockBridger

Helper plugin that allows Bukkit/Spigot/Paper plugins to interact with a Geyser proxy and provide custom integration
with Bedrock clients.

It provides Bedrock session detection, packet listeners capabilities irrespective of where the Geyser proxy is located,
and mappings between Bukkit Materials and Bedrock textures with automatic retrieval and parsing of Bedrock texture
packs, with a focus to decouple the underlying Geyser Connector implementation from its API.

**⚠ This code is bad, really bad, basing itself on unstable APIs, using dirty hacks and an ugly architecture. Don't use this yet.**

## License

BedrockBridger is distributed under the LGPL version 3 license. A copy of the license is provided in LICENSE.md.

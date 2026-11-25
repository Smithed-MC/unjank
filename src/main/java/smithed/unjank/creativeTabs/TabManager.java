package smithed.unjank.creativeTabs;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import smithed.unjank.Unjank;
import smithed.unjank.UnjankRegistries;
import smithed.unjank.creativeTabs.sources.ResolvedItemSource;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TabManager {
    private static ClientboundTabsPayload lastSyncPacket;

    public static void initialize() {
        PayloadTypeRegistry.clientboundPlay().register(ClientboundTabsPayload.TYPE, ClientboundTabsPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ServerboundRequestTabsPayload.TYPE, ServerboundRequestTabsPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ServerboundRequestTabsPayload.TYPE, ((payload, context) -> {
            sendLastSyncPacket(context.player());
        }));

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, _, _) -> {
            broadcastCreativeMenus(server);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(TabManager::broadcastCreativeMenus);
    }

    private static Set<Identifier> getRegisteredTabs() {
        return BuiltInRegistries.CREATIVE_MODE_TAB.keySet();
    }

    private static HashMap<Identifier, CreativeTabData> getDynamicTabs(MinecraftServer server) {
        var registry = server.reloadableRegistries().lookup().lookup(UnjankRegistries.CREATIVE_TABS_REGISTRY_KEY);

        if (registry.isEmpty()) return new HashMap<>();

        var tabs = new HashMap<Identifier, CreativeTabData>();
        registry.get().listElementIds().forEach(id -> {
            tabs.put(id.identifier(), registry.get().get(id).get().value().toData(server));
        });

        return tabs;
    }

    private static void broadcastCreativeMenus(MinecraftServer server) {
        var itemSourceRegistry = server.reloadableRegistries().lookup().lookup(UnjankRegistries.ITEM_SOURCE_REGISTRY_KEY);

        var tabAdditions = new HashMap<Identifier, List<ResolvedItemSource>>();

        // For every registered tab on the server
        // Find the matching ItemSource tag `<namespace>:tags/unjank/item_source/<id>`
        // and compile the results
        for (var tab : getRegisteredTabs()) {
            var additions = itemSourceRegistry.get().get(TagKey.create(UnjankRegistries.ITEM_SOURCE_REGISTRY_KEY, tab));

            additions.ifPresent(items ->
                    tabAdditions.put(
                            tab,
                            items
                                    .stream()
                                    .flatMap(i -> i.value().resolve(server).stream())
                                    .toList()
                    )
            );
        }


        var dynamicTabs = getDynamicTabs(server);

        Unjank.LOGGER.info("Loaded {} creative dynamicTabs", dynamicTabs.size());

        lastSyncPacket = new ClientboundTabsPayload(tabAdditions, dynamicTabs);

        for (var player : server.getPlayerList().getPlayers()) {
            sendLastSyncPacket(player);
        }
    }

    private static void sendLastSyncPacket(ServerPlayer player) {
        if (lastSyncPacket == null) {
            Unjank.LOGGER.warn("Attempted to send the creative dynamicTabs before they were loaded");
            return;
        }

        if (ServerPlayNetworking.canSend(player, ClientboundTabsPayload.TYPE))
            ServerPlayNetworking.send(player, lastSyncPacket);
    }
}

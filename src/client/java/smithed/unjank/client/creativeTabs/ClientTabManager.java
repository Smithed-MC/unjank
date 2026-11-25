package smithed.unjank.client.creativeTabs;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.client.Minecraft;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import smithed.unjank.Unjank;
import smithed.unjank.creativeTabs.ClientboundTabsPayload;
import smithed.unjank.creativeTabs.CreativeTabData;
import smithed.unjank.creativeTabs.ServerboundRequestTabsPayload;
import smithed.unjank.creativeTabs.sources.ResolvedItemSource;
import smithed.unjank.mixin.client.CreativeModeTabAccessor;
import smithed.unjank.mixin.client.CreativeModeTabsAccessor;
import smithed.unjank.mixin.client.CreativeModeInventoryScreenAccessor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ClientTabManager {
    private static final HashMap<Identifier, TabState> registeredTabStates = new HashMap<>();
    private static final HashMap<Identifier, List<ResolvedItemSource>> tabAdditions = new HashMap<>();
    private static final HashMap<CreativeModeTab, Identifier> tabInstanceToId = new HashMap<>();

    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundTabsPayload.TYPE, ((payload, context) -> {
            Unjank.LOGGER.info("Received {} updated dynamicTabs", payload.dynamicTabs().size());
            updateTabs(context.player().registryAccess(), payload.tabAdditions(), payload.dynamicTabs());
        }));

        ClientPlayConnectionEvents.JOIN.register((ctx, _, _) -> {
            hideOldTabs(ctx.registryAccess());
            if (ClientPlayNetworking.canSend(ServerboundRequestTabsPayload.TYPE)) {
                Unjank.LOGGER.info("Requesting updated dynamicTabs");
                ClientPlayNetworking.send(ServerboundRequestTabsPayload.INSTANCE);
            }
        });
    }

    private static void withMutability(RegistryAccess registryAccess, Consumer<Registry<CreativeModeTab>> func) {
        MappedRegistry<CreativeModeTab> registry = (MappedRegistry<CreativeModeTab>) registryAccess.lookup(Registries.CREATIVE_MODE_TAB).get();
        smithed.unjank.mixin.MappedRegistryAccessor accessor = (smithed.unjank.mixin.MappedRegistryAccessor) registry;

        boolean wasFrozen = accessor.isFrozen();
        accessor.setFrozen(false);

        try {
            func.accept(registry);
        } finally {
            accessor.setFrozen(wasFrozen);
        }

    }

    private static void hideOldTabs(RegistryAccess registryAccess) {
        tabAdditions.clear();
        tabInstanceToId.clear();

        withMutability(registryAccess, (registry) -> {
            for (var state : registeredTabStates.entrySet()) {
                state.getValue().setActive(false);
            }

            CreativeModeInventoryScreenAccessor.setSelectedTab(CreativeModeTabs.getDefaultTab());
        });

        rebuildTabs();
    }

    private static void updateTabs(RegistryAccess registryAccess, HashMap<Identifier, List<ResolvedItemSource>> updatedAdditions, HashMap<Identifier, CreativeTabData> updatedTabs) {
        tabAdditions.clear();
        tabAdditions.putAll(updatedAdditions);

        for (var id : tabAdditions.keySet()) {
            var tab = BuiltInRegistries.CREATIVE_MODE_TAB.get(id);
            tab.ifPresent(t -> tabInstanceToId.put(t.value(), id));
        }

        withMutability(registryAccess, (registry) -> {
            for (var tab : registeredTabStates.entrySet()) {
                tab.getValue().setActive(updatedTabs.containsKey(tab.getKey()));
            }

            for (var configEntry : updatedTabs.entrySet()) {

                var identifier = configEntry.getKey();
                var config = configEntry.getValue();

                registeredTabStates.computeIfAbsent(identifier, _ -> new TabState(config.items()))
                        .setIcon(config.icon().create())
                        .setItems(config.items())
                        .setActive(true);

                if (!registry.containsKey(identifier)) {
                    var tab = FabricCreativeModeTab.builder()
                            .title(config.title())
                            .icon(() -> generateIcon(identifier))
                            .displayItems((displayParameters, output) -> generateDisplayItems(identifier, displayParameters, output))
                            .build();

                    Registry.register(registry, identifier, tab);
                } else {
                    var tab = (CreativeModeTabAccessor) registry.get(identifier).get().value();

                    tab.setDisplayName(config.title());
                    tab.setIconItemStack(config.icon().create());
                }
            }
        });

        rebuildTabs();
    }

    private static void generateDisplayItems(Identifier tabId, CreativeModeTab.ItemDisplayParameters displayParameters, CreativeModeTab.Output output) {
        var state = registeredTabStates.get(tabId);
        if (!state.isActive()) return;

        output.acceptAll(ResolvedItemSource.applyAll(state.items, List.of(), displayParameters.hasPermissions()));
    }

    private static ItemStack generateIcon(Identifier tabId) {
        var state = registeredTabStates.get(tabId);
        return state.getIcon();
    }

    private static void rebuildTabs() {
        var client = Minecraft.getInstance();

        if (client.level != null) {
            var params = new CreativeModeTab.ItemDisplayParameters(client.level.enabledFeatures(), client.options.operatorItemsTab().get(), client.level.registryAccess());

            CreativeModeTabsAccessor.setCachedParameters(params);
            CreativeModeTabsAccessor.buildAllTabContents(params);

            var searchItems = List.copyOf(CreativeModeTabs.searchTab().getSearchTabDisplayItems());
            var searchTrees = client.getConnection().searchTrees();
            searchTrees.updateCreativeTooltips(client.level.registryAccess(), searchItems);
            searchTrees.updateCreativeTags(searchItems);
        }
    }

    public static Collection<ItemStack> getDisplayItemsWithAdditions(CreativeModeTab tab, Collection<ItemStack> baseItems, CreativeModeTab.ItemDisplayParameters params) {
        if (!tabInstanceToId.containsKey(tab))
            return baseItems;

        var items = tabAdditions.get(tabInstanceToId.get(tab));

        return ResolvedItemSource.applyAll(items, baseItems, params.hasPermissions());
    }

    public static void getSearchTabItemsWithAdditions(CreativeModeTab tab, Set<ItemStack> baseItems, CreativeModeTab.ItemDisplayParameters params) {
        if (!tabInstanceToId.containsKey(tab))
            return;

        var items = tabAdditions.get(tabInstanceToId.get(tab));

        for (var item : items) {
            if (item.operator().orElse(false) && !params.hasPermissions())
                continue;

            baseItems.addAll(item.items());
        }
    }

    private static class TabState {
        private boolean active;

        private ItemStack icon;
        private List<ResolvedItemSource> items;

        public TabState(List<ResolvedItemSource> items) {
            this.items = items;
        }

        public TabState setActive(boolean active) {
            this.active = active;
            return this;
        }

        public boolean isActive() {
            return this.active;
        }

        public TabState setItems(List<ResolvedItemSource> items) {
            this.items = items;
            return this;
        }

        public List<ResolvedItemSource> getItems() {
            return this.items;
        }

        public TabState setIcon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        public ItemStack getIcon() {
            return this.icon;
        }
    }
}

package smithed.unjank;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import smithed.unjank.creativeTabs.CreativeTabConfig;
import smithed.unjank.creativeTabs.sources.ItemSource;
import smithed.unjank.creativeTabs.sources.ItemSourceTypes;

public class UnjankRegistries {
    public static final ResourceKey<Registry<CreativeTabConfig>> CREATIVE_TABS_REGISTRY_KEY =
            ResourceKey.createRegistryKey(Unjank.id("creative_tab"));

    public static final ResourceKey<Registry<ItemSource>> ITEM_SOURCE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(Unjank.id("item_source"));

    public static void initialize() {
        DynamicRegistries.registerReloadable(CREATIVE_TABS_REGISTRY_KEY, CreativeTabConfig.CODEC);
        DynamicRegistries.registerReloadable(ITEM_SOURCE_REGISTRY_KEY, ItemSourceTypes.CODEC);
    }
}

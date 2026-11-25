package smithed.unjank.creativeTabs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStackTemplate;
import smithed.unjank.UnjankRegistries;
import smithed.unjank.creativeTabs.sources.ItemSource;
import smithed.unjank.creativeTabs.sources.ItemSourceTypes;

public class CreativeTabConfig {
    public static final Codec<CreativeTabConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStackTemplate.CODEC.fieldOf("icon").forGetter(CreativeTabConfig::getItemStack),
            ComponentSerialization.CODEC.fieldOf("title").forGetter(CreativeTabConfig::getTitle),
            RegistryCodecs.holderSet(UnjankRegistries.ITEM_SOURCE_REGISTRY_KEY, ItemSourceTypes.CODEC).fieldOf("items").forGetter(CreativeTabConfig::getItems)
    ).apply(instance, CreativeTabConfig::new));


    private final ItemStackTemplate icon;
    private final Component title;
    private final HolderSet<ItemSource> items;

    public ItemStackTemplate getItemStack() {
        return icon;
    }

    public Component getTitle() {
        return title;
    }

    public HolderSet<ItemSource> getItems() {
        return items;
    }

    public CreativeTabConfig(ItemStackTemplate icon, Component title, HolderSet<ItemSource> items) {
        this.icon = icon;
        this.title = title;
        this.items = items;
    }

    public CreativeTabData toData(MinecraftServer server) {
        return new CreativeTabData(
                this.icon,
                this.title,
                ItemSource.resolveAll(server, this.items)
        );
    }
}

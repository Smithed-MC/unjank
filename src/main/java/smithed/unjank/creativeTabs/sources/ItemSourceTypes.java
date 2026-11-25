package smithed.unjank.creativeTabs.sources;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import smithed.unjank.Unjank;

public class ItemSourceTypes {
    public static final Codec<ItemSource> CODEC = Codec.recursive("ItemSource", self -> ItemSourceType.REGISTRY.byNameCodec().dispatch("type", ItemSource::getType, ItemSourceType::codec));

    public static final ItemSourceType<ItemTemplateSource> ITEM_TEMPLATE_SOURCE = register("item", new ItemSourceType<>(ItemTemplateSource.CODEC));
    public static final ItemSourceType<LootSource> LOOT_SOURCE = register("loot", new ItemSourceType<>(LootSource.CODEC));
    public static final ItemSourceType<GroupSource> GROUP_SOURCE = register("group", new ItemSourceType<>(GroupSource.CODEC));

    public static <T extends ItemSource> ItemSourceType<T> register(String id, ItemSourceType<T> itemSourceType) {
        return Registry.register(ItemSourceType.REGISTRY, Unjank.id(id), itemSourceType);
    }
}

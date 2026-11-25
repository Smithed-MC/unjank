package smithed.unjank.creativeTabs;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import smithed.unjank.creativeTabs.sources.ResolvedItemSource;

import java.util.List;

public record CreativeTabData(ItemStackTemplate icon, Component title, List<ResolvedItemSource> items) {
    public static final StreamCodec<RegistryFriendlyByteBuf, CreativeTabData> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC,
            CreativeTabData::icon,
            ComponentSerialization.STREAM_CODEC,
            CreativeTabData::title,
            ResolvedItemSource.LIST_STREAM_CODEC,
            CreativeTabData::items,
            CreativeTabData::new
    );

}

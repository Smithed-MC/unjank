package smithed.unjank.creativeTabs;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import smithed.unjank.Unjank;
import smithed.unjank.creativeTabs.sources.ResolvedItemSource;

import java.util.HashMap;
import java.util.List;

public record ClientboundTabsPayload(HashMap<Identifier, List<ResolvedItemSource>> tabAdditions, HashMap<Identifier, CreativeTabData> dynamicTabs) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundTabsPayload> TYPE = new CustomPacketPayload.Type<>(Unjank.id("tabs"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTabsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    Identifier.STREAM_CODEC,
                    ResolvedItemSource.LIST_STREAM_CODEC
            ),
            ClientboundTabsPayload::tabAdditions,
            ByteBufCodecs.map(
                    HashMap::new,
                    Identifier.STREAM_CODEC,
                    CreativeTabData.STREAM_CODEC
            ),
            ClientboundTabsPayload::dynamicTabs,
            ClientboundTabsPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

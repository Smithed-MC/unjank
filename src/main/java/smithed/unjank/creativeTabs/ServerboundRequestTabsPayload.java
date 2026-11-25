package smithed.unjank.creativeTabs;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;
import smithed.unjank.Unjank;

public class ServerboundRequestTabsPayload implements CustomPacketPayload {
    public static final ServerboundRequestTabsPayload INSTANCE = new ServerboundRequestTabsPayload();

    public static final CustomPacketPayload.Type<ServerboundRequestTabsPayload> TYPE = new CustomPacketPayload.Type<>(Unjank.id("request_tabs"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestTabsPayload> STREAM_CODEC = StreamCodec.unit(
           INSTANCE
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

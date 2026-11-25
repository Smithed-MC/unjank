package smithed.unjank.creativeTabs.sources;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import smithed.unjank.Unjank;

public record ItemSourceType<T extends ItemSource>(MapCodec<T> codec) {
    public static final Registry<ItemSourceType<?>> REGISTRY = new MappedRegistry<>(
            ResourceKey.createRegistryKey(Unjank.id("item_source")), Lifecycle.stable()
    );
}

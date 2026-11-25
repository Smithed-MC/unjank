package smithed.unjank.creativeTabs.sources;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

public class LootSource extends ItemSource {
    public static final MapCodec<LootSource> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i)
            .and(
                    RegistryCodecs.holderSet(Registries.LOOT_TABLE).fieldOf("loot").forGetter(LootSource::getLoot)
            )
            .apply(i, LootSource::new)
    );

    private final HolderSet<LootTable> loot;

    public HolderSet<LootTable> getLoot() {
        return this.loot;
    }

    public LootSource(Optional<Boolean> operator, Optional<ItemPredicate> after, HolderSet<LootTable> loot) {
        super(operator, after);
        this.loot = loot;
    }

    @Override
    public MapCodec<? extends ItemSource> codec() {
        return CODEC;
    }

    @Override
    public Collection<ItemStack> getResult(MinecraftServer server) {
        var items = new ArrayList<ItemStack>();

        var params = new LootParams(
                Objects.requireNonNull(server.getLevel(Level.OVERWORLD)),
                ContextMap.builder().build(),
                new HashMap<>(), 1.0f
        );

        for (var table : this.loot) {
            var result = new ArrayList<ItemStack>();
            table.value().getRandomItems(params, item -> {
                item.setCount(1);
                if (result.isEmpty())
                    result.add(item);
            });
            items.addAll(result);
        }

        return items;
    }

    @Override
    public ItemSourceType<?> getType() {
        return ItemSourceTypes.LOOT_SOURCE;
    }
}

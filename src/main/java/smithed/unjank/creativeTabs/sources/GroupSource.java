package smithed.unjank.creativeTabs.sources;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import smithed.unjank.UnjankRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class GroupSource extends ItemSource {
    public static final MapCodec<GroupSource> CODEC = RecordCodecBuilder.mapCodec(
            i -> commonFields(i)
                    .and(
                            RegistryCodecs.holderSet(UnjankRegistries.ITEM_SOURCE_REGISTRY_KEY, ItemSourceTypes.CODEC).fieldOf("items").forGetter(GroupSource::getItems)
                    ).apply(i, GroupSource::new)
    );

    private final HolderSet<ItemSource> items;

    public HolderSet<ItemSource> getItems() {
        return this.items;
    }

    public GroupSource(Optional<Boolean> operator, Optional<ItemPredicate> after, HolderSet<ItemSource> items) {
        super(operator, after);
        this.items = items;
    }

    @Override
    public MapCodec<? extends ItemSource> codec() {
        return CODEC;
    }

    @Override
    public Collection<ItemStack> getResult(MinecraftServer server) {
        var items = new ArrayList<ItemStack>();

        for (var item : this.items) {
            items.addAll(item.value().getResult(server));
        }

        return items;
    }

    @Override
    public List<ResolvedItemSource> resolve(MinecraftServer server) {
        var resolved = new ArrayList<ResolvedItemSource>();
        for (var child : this.items) {
            for (var entry : child.value().resolve(server)) {
                var effectiveOp = entry.operator().isPresent() ? entry.operator() : this.operator;
                var effectiveAfter = entry.after().isPresent() ? entry.after() : this.after;
                resolved.add(new ResolvedItemSource(effectiveOp, effectiveAfter, entry.items()));
            }
        }
        return resolved;
    }

    @Override
    public ItemSourceType<?> getType() {
        return ItemSourceTypes.GROUP_SOURCE;
    }
}

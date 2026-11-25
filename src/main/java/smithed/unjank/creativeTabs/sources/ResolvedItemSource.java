package smithed.unjank.creativeTabs.sources;

import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record ResolvedItemSource(
        Optional<Boolean> operator,
        Optional<ItemPredicate> after,
        List<ItemStack> items
) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ResolvedItemSource> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL.apply(ByteBufCodecs::optional), ResolvedItemSource::operator,
                    ByteBufCodecs.fromCodecWithRegistries(ItemPredicate.CODEC).apply(ByteBufCodecs::optional), ResolvedItemSource::after,
                    ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), ResolvedItemSource::items,
                    ResolvedItemSource::new
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<ResolvedItemSource>> LIST_STREAM_CODEC =
            STREAM_CODEC.apply(ByteBufCodecs.list());

    public static Collection<ItemStack> applyAll(
            List<ResolvedItemSource> sources,
            Collection<ItemStack> initialItems,
            boolean hasOperatorTab
    ) {
        var output = new ArrayList<>(initialItems);

        ItemPredicate lastPredicate = null;
        int lastInsertEnd = -1;

        for (var source : sources) {
            if (source.operator().isPresent() && source.operator().get() != hasOperatorTab) {
                continue;
            }

            if (source.items().isEmpty()) {
                continue;
            }

            if (source.after().isEmpty()) {
                output.addAll(source.items());
                lastPredicate = null;
                lastInsertEnd = -1;
            } else {
                var predicate = source.after().get();
                int insertAt;

                if (predicate.equals(lastPredicate) && lastInsertEnd != -1) {
                    insertAt = lastInsertEnd;
                } else {
                    int found = -1;
                    for (int idx = 0; idx < output.size(); idx++) {
                        if (predicate.test(output.get(idx))) {
                            found = idx;
                            break;
                        }
                    }
                    insertAt = (found == -1) ? output.size() : found + 1;
                }

                output.addAll(insertAt, source.items());
                lastPredicate = predicate;
                lastInsertEnd = insertAt + source.items().size();
            }
        }

        // Lazily depupe
        var set = ItemStackLinkedSet.createTypeAndComponentsSet();
        set.addAll(output);

        return set;
    }
}
package smithed.unjank.creativeTabs.sources;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class ItemSource {
    public abstract MapCodec<? extends ItemSource> codec();

    public abstract Collection<ItemStack> getResult(MinecraftServer server);

    public abstract ItemSourceType<?> getType();

    protected static <T extends ItemSource> Products.P2<RecordCodecBuilder.Mu<T>, Optional<Boolean>, Optional<ItemPredicate>> commonFields(final RecordCodecBuilder.Instance<T> i) {
        return i.group(
                Codec.BOOL.optionalFieldOf("operator").forGetter(ItemSource::getOperator),
                ItemPredicate.CODEC.optionalFieldOf("after").forGetter(ItemSource::getAfter)
        );
    }

    public Optional<Boolean> operator;
    public Optional<ItemPredicate> after;

    public Optional<ItemPredicate> getAfter() {
        return this.after;
    }

    public Optional<Boolean> getOperator() {
        return this.operator;
    }

    public ItemSource(Optional<Boolean> operator, Optional<ItemPredicate> after) {
        this.operator = operator;
        this.after = after;
    }

    public List<ResolvedItemSource> resolve(MinecraftServer server) {
        var items = List.copyOf(getResult(server));
        if (items.isEmpty()) {
            return List.of();
        }
        return List.of(new ResolvedItemSource(this.operator, this.after, items));
    }

    public static List<ResolvedItemSource> resolveAll(MinecraftServer server, Iterable<Holder<ItemSource>> sources) {
        var resolved = new ArrayList<ResolvedItemSource>();
        for (var holder : sources) {
            resolved.addAll(holder.value().resolve(server));
        }
        return resolved;
    }
}

package smithed.unjank.creativeTabs.sources;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemTemplateSource extends ItemSource {
    public static final MapCodec<ItemTemplateSource> CODEC = RecordCodecBuilder.mapCodec(i ->
            commonFields(i)
                    .and(
                        ItemStackTemplate.CODEC.fieldOf("item").forGetter(ItemTemplateSource::getItem)
                    )
                    .apply(i, ItemTemplateSource::new)
    );

    private final ItemStackTemplate item;

    public ItemTemplateSource(Optional<Boolean> operator, Optional<ItemPredicate> after, ItemStackTemplate item) {
        super(operator, after);
        this.item = item;
    }

    public ItemStackTemplate getItem() {
        return this.item;
    }

    @Override
    public MapCodec<? extends ItemSource> codec() {
        return CODEC;
    }

    @Override
    public Collection<ItemStack> getResult(MinecraftServer server) {
        var result = this.item.create();
        result.setCount(1);
        return List.of(result);
    }

    @Override
    public ItemSourceType<?> getType() {
        return ItemSourceTypes.ITEM_TEMPLATE_SOURCE;
    }
}

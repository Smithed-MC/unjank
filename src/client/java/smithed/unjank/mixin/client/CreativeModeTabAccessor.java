package smithed.unjank.mixin.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreativeModeTab.class)
public interface CreativeModeTabAccessor {
    @Mutable
    @Accessor("displayName")
    void setDisplayName(Component displayName);

    @Accessor("iconItemStack")
    void setIconItemStack(@Nullable ItemStack iconItemStack);
}

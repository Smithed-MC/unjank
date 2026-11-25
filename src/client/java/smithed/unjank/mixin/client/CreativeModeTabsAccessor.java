package smithed.unjank.mixin.client;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreativeModeTabs.class)
public interface CreativeModeTabsAccessor {
    @Accessor("CACHED_PARAMETERS")
    static void setCachedParameters(final CreativeModeTab.ItemDisplayParameters parameters) {

    }

    @Invoker("buildAllTabContents")
    static void buildAllTabContents(final CreativeModeTab.ItemDisplayParameters parameters) {}
}

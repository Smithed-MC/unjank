package smithed.unjank.mixin.client;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import smithed.unjank.client.creativeTabs.ClientTabManager;

import java.util.Collection;
import java.util.Set;

@Mixin(CreativeModeTab.class)
public abstract class CreativeModeTabMixin {
    @Shadow
    private Collection<ItemStack> displayItems;
    @Shadow
    private Set<ItemStack> displayItemsSearchTab;

    @Shadow
    public abstract Collection<ItemStack> getDisplayItems();

    @Inject(method = "buildContents", at = @At("TAIL"))
    private void unjank$buildContents(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo ci) {
        displayItems = ClientTabManager.getDisplayItemsWithAdditions((CreativeModeTab) (Object) this, displayItems, parameters);
        ClientTabManager.getSearchTabItemsWithAdditions((CreativeModeTab) (Object) this, displayItemsSearchTab, parameters);
    }
}

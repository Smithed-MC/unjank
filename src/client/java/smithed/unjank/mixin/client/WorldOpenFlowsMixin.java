package smithed.unjank.mixin.client;

import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import smithed.unjank.Unjank;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {
    @Inject(method = "askForBackup", at = @At("HEAD"))
    private void onAskForBackup(LevelStorageSource.LevelStorageAccess levelAccess, boolean oldCustomized, Runnable proceedCallback, Runnable cancelCallback, CallbackInfo ci) {
        if (Unjank.CONFIG.disableExperimentalSettingsWarning()) {
            proceedCallback.run();
        }
    }
}

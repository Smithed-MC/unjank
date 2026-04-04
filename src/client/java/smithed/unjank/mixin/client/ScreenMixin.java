package smithed.unjank.mixin.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow
    public abstract void onClose();

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (event.isEscape() && event.hasAltDown()) {
            this.onClose();
            cir.setReturnValue(true);
        }
    }
}

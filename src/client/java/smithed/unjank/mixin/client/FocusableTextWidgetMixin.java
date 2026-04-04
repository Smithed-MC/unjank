package smithed.unjank.mixin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import smithed.unjank.Unjank;

@Mixin(FocusableTextWidget.class)
public class FocusableTextWidgetMixin {

    @Redirect(method = "extractWidgetRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;outline(IIIII)V"))
    private void outline(GuiGraphicsExtractor instance, int x, int y, int width, int height, int color) {
        if (Unjank.CONFIG.disableFocusBorder())
            return;

        instance.outline(x, y, width, height, color);
    }
}
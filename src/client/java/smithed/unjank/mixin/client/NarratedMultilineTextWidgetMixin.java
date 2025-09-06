package smithed.unjank.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.NarratedMultilineTextWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import smithed.unjank.Unjank;

@Mixin(NarratedMultilineTextWidget.class)
public class NarratedMultilineTextWidgetMixin {

    @Redirect(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawBorder(IIIII)V"))
    private void drawBorders(DrawContext context, int x, int y, int width, int height, int color) {
        if (Unjank.CONFIG.disableFocusBorder())
            return;

        context.drawBorder(x, y, width, height, color);
    }
}
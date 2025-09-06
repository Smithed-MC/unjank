package smithed.unjank.mixin.client;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.dialog.DialogScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import smithed.unjank.Unjank;

@Mixin(DialogScreen.class)
public abstract class DialogScreenMixin extends Screen {
    @Shadow
    private ButtonWidget warningButton;

    protected DialogScreenMixin(Text title) {
        super(title);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/dialog/DialogScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    private <T extends Element & Drawable & Selectable> T addDrawableChild(DialogScreen screen, T element) {
        if (Unjank.CONFIG.disableWarningBox() && element == warningButton)
            return element;

        return this.addDrawableChild(element);
    }

    @Redirect(method = "createHeader", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;"))
    private Widget add(DirectionalLayoutWidget layout, Widget widget) {
        if (Unjank.CONFIG.disableWarningBox() && widget == this.warningButton)
            return widget;

        return layout.add(widget);
     }
}

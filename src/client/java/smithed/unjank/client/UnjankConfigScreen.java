package smithed.unjank.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import smithed.unjank.Unjank;

public class UnjankConfigScreen extends Screen {
    private final Screen parent;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public UnjankConfigScreen(Screen parent) {
        super(Component.literal("Unjank Configuration"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(this.title, this.font);

        LinearLayout contentLayout = this.layout.addToContents(LinearLayout.vertical().spacing(8));
        contentLayout.defaultCellSetting().alignHorizontallyCenter();

        contentLayout.addChild(
            CycleButton.onOffBuilder(Unjank.CONFIG.disableFocusBorder)
                .create(0, 0, 250, 20, Component.literal("Disable Focus Border"), (button, value) -> {
                    Unjank.CONFIG.disableFocusBorder = value;
                    Unjank.CONFIG.save();
                })
        );

        contentLayout.addChild(
            CycleButton.onOffBuilder(Unjank.CONFIG.disableWarningBox)
                .create(0, 0, 250, 20, Component.literal("Disable Warning Box"), (button, value) -> {
                    Unjank.CONFIG.disableWarningBox = value;
                    Unjank.CONFIG.save();
                })
        );

        contentLayout.addChild(
            CycleButton.onOffBuilder(Unjank.CONFIG.disableCommandWarning)
                .create(0, 0, 250, 20, Component.literal("Disable Command Warning"), (button, value) -> {
                    Unjank.CONFIG.disableCommandWarning = value;
                    Unjank.CONFIG.save();
                })
        );

        this.layout.addToFooter(
            Button.builder(Component.translatable("gui.done"), button -> this.onClose())
                .width(200)
                .build()
        );

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void onClose() {
        Unjank.CONFIG.save();
        if (this.minecraft != null) {
            this.minecraft.setScreenAndShow(this.parent);
        }
    }
}

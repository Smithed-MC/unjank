package smithed.unjank.mixin.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import smithed.unjank.Unjank;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl {

    protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
        super(minecraft, connection, cookie);
    }

    @Inject(method = "openCommandSendConfirmationWindow", at = @At(value = "HEAD"), cancellable = true)
    private void openCommandSendConfirmationWindow(String command, String messageKey, Screen screenAfterCommand, CallbackInfo ci) {
        if (Unjank.CONFIG.disableCommandWarning()) {
            this.send(new ServerboundChatCommandPacket(command));
            this.minecraft.setScreenAndShow(screenAfterCommand);
            ci.cancel();
        }
    }
}

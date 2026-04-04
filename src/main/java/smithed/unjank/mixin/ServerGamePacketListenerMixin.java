package smithed.unjank.mixin;

import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerMixin {

    @Shadow
    protected abstract void performUnsignedChatCommand(String command);

    @Shadow
    protected abstract void performSignedChatCommand(ServerboundChatCommandSignedPacket packet, LastSeenMessages lastSeenMessages);

    @Inject(method = "lambda$handleChatCommand$0", at = @At("HEAD"), cancellable = true)
    private void handleChatCommand(ServerboundChatCommandPacket packet, CallbackInfo ci) {
        if (packet.command().startsWith("trigger")) {
            this.performUnsignedChatCommand(packet.command());
            ci.cancel();
        }
    }

    @Inject(method = "lambda$handleSignedChatCommand$0", at = @At("HEAD"), cancellable = true)
    private void handleSignedChatCommand(ServerboundChatCommandSignedPacket packet, Optional<LastSeenMessages> unpackedLastSeen, CallbackInfo ci) {
        if (packet.command().startsWith("trigger") && unpackedLastSeen.isPresent()) {
            this.performSignedChatCommand(packet, unpackedLastSeen.get());
            ci.cancel();
        }
    }
}

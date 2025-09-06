package smithed.unjank.mixin;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TriggerCommand;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import smithed.unjank.Unjank;

import java.util.function.Supplier;

@Mixin(TriggerCommand.class)
public abstract class TriggerCommandMixin {

    @Redirect(method = "executeAdd", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V"))
    private static void executeAddFeedback(ServerCommandSource source, Supplier<Text> feedbackSupplier, boolean broadcastToOps) {
        sendConditionalFeedback(source, feedbackSupplier, broadcastToOps);
    }

    @Redirect(method = "executeSet", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V"))
    private static void executeSetFeedback(ServerCommandSource source, Supplier<Text> feedbackSupplier, boolean broadcastToOps) {
        sendConditionalFeedback(source, feedbackSupplier, broadcastToOps);
    }

    @Redirect(method = "executeSimple", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Ljava/util/function/Supplier;Z)V"))
    private static void executeSimpleFeedback(ServerCommandSource source, Supplier<Text> feedbackSupplier, boolean broadcastToOps) {
        sendConditionalFeedback(source, feedbackSupplier, broadcastToOps);
    }

    @Unique
    private static void sendConditionalFeedback(ServerCommandSource source, Supplier<Text> feedbackSupplier, boolean broadcastToOps) {
        var value = source.getWorld().getGameRules().get(Unjank.SEND_TRIGGER_FEEDBACK).get();
        if (value != Unjank.TriggerFeedback.disabled) {
            source.sendFeedback(feedbackSupplier, value == Unjank.TriggerFeedback.sourceAndOps);
        }
    }
}

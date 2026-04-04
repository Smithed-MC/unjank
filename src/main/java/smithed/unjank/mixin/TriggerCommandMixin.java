package smithed.unjank.mixin;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.TriggerCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import smithed.unjank.Unjank;

import java.util.function.Supplier;

@Mixin(TriggerCommand.class)
public abstract class TriggerCommandMixin {

    @Redirect(method = "addValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Ljava/util/function/Supplier;Z)V"))
    private static void addValueFeedback(CommandSourceStack source, Supplier<Component> feedbackSupplier, boolean broadcastToOps) {
        sendConditionalFeedback(source, feedbackSupplier);
    }

    @Redirect(method = "setValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Ljava/util/function/Supplier;Z)V"))
    private static void setValueFeedback(CommandSourceStack source, Supplier<Component> feedbackSupplier, boolean broadcastToOps) {
        sendConditionalFeedback(source, feedbackSupplier);
    }

    @Redirect(method = "simpleTrigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Ljava/util/function/Supplier;Z)V"))
    private static void executeSimpleFeedback(CommandSourceStack source, Supplier<Component> feedbackSupplier, boolean broadcastToOps) {
        sendConditionalFeedback(source, feedbackSupplier);
    }

    @Unique
    private static void sendConditionalFeedback(CommandSourceStack source, Supplier<Component> feedbackSupplier) {
        var value = source.getLevel().getGameRules().get(Unjank.SEND_TRIGGER_FEEDBACK);
        if (value != Unjank.TriggerFeedback.disabled) {
            source.sendSuccess(feedbackSupplier, value == Unjank.TriggerFeedback.sourceAndOps);
        }
    }
}

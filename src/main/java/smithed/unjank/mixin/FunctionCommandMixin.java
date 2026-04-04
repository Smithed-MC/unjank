package smithed.unjank.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.commands.FunctionCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.CommonColors;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import smithed.unjank.Unjank;

import java.util.ArrayList;
import java.util.List;

@Mixin(FunctionCommand.class)
public class FunctionCommandMixin {
    @Unique
    private static final DynamicCommandExceptionType ERROR = new DynamicCommandExceptionType((o) -> (Message) o);

    @Inject(method = "instantiateAndQueueFunctions", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/exceptions/Dynamic2CommandExceptionType;create(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;"))
    private static <T extends ExecutionCommandSource<T>> void broadcastError(@Nullable CompoundTag arguments, ExecutionControl<T> output, CommandDispatcher<T> dispatcher, T noCallbackSource, CommandFunction<T> function, Identifier id, CommandResultCallback functionResultCollector, boolean returnParentFrame, CallbackInfo ci, @Local(name = "exception") FunctionInstantiationException exception) {
        if (noCallbackSource instanceof CommandSourceStack context) {
            logException(arguments, id, exception, context);
        }
    }

    @Redirect(method = "instantiateAndQueueFunctions", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/exceptions/Dynamic2CommandExceptionType;create(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;"))
    private static CommandSyntaxException defaultError(Dynamic2CommandExceptionType instance, Object id, Object reason, @Local @Nullable CompoundTag args) {
        return ERROR.create(createComponent((Identifier) id, args, (Component) reason));
    }

    @Unique
    private static void logException(@Nullable CompoundTag arguments, Identifier id, FunctionInstantiationException exception, CommandSourceStack context) {
        var server = context.getServer();

        try {
            var message = ComponentUtils.formatList(List.of(Component.literal("[Unjank] ").withColor(CommonColors.LIGHT_GRAY), createComponent(id, arguments, exception.messageComponent())), Component.literal(" "));

            if (server.getGameRules().get(Unjank.BROADCAST_MACRO_FAILURE)) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (player.commandSource() != context.source && server.getPlayerList().isOp(player.nameAndId())) {
                        player.sendSystemMessage(message);
                    }
                }
            }
        } catch (Exception e) {
            Unjank.LOGGER.error("Failed to make error message: {}", e.getMessage());
        } finally {
            if (server.getGameRules().get(Unjank.LOG_MACRO_FAILURE)) {
                Unjank.LOGGER.error("Failed to instantiate function {}: {}\nArguments: {}", id, exception.toString(), arguments);
            }
        }
    }

    private static @NonNull Component createComponent(Identifier id, @Nullable CompoundTag arguments, Component reason) {
        var argumentList = new ArrayList<Component>();
        argumentList.add(Component.literal(""));

        if (arguments != null) {
            for (var kv : arguments.entrySet()) {
                argumentList.add(
                        ComponentUtils.formatList(
                                List.of(
                                        Component.literal(" - ").withStyle(ChatFormatting.GRAY),
                                        Component.literal(kv.getKey()).withStyle(ChatFormatting.AQUA),
                                        Component.literal(": ").withStyle(ChatFormatting.GRAY),
                                        NbtUtils.toPrettyComponent(kv.getValue())
                                ),
                                Component.literal("")
                        )
                );
            }
        }

        var debug = ComponentUtils.formatList(List.of(
                Component.literal("Function: ").withStyle(ChatFormatting.GRAY),
                Component.literal(id.toString() + "\n"),
                Component.literal("Arguments:").withStyle(ChatFormatting.GRAY),
                arguments != null ? ComponentUtils.formatList(argumentList, Component.literal("\n")) : Component.literal(" None")
        ), Component.literal(""));

        var hover = new HoverEvent.ShowText(debug);

        return Component.translatableEscape("commands.function.instantiationFailure", id, reason).withStyle(style -> style.withColor(ChatFormatting.RED).withHoverEvent(hover));
    }
}

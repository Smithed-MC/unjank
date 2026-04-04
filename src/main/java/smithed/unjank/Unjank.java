package smithed.unjank;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unjank implements ModInitializer {
    public static final String MOD_ID = "unjank";
    public static final smithed.unjank.UnjankConfig CONFIG = smithed.unjank.UnjankConfig.createAndLoad();

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final GameRule<TriggerFeedback> SEND_TRIGGER_FEEDBACK = GameRuleBuilder
            .forEnum(TriggerFeedback.sourceAndOps)
            .category(GameRuleCategory.CHAT)
            .buildAndRegister(Identifier.fromNamespaceAndPath(MOD_ID, "send_trigger_feedback"));


    public static final GameRule<Boolean> BROADCAST_MACRO_FAILURE = GameRuleBuilder
            .forBoolean(false)
            .category(GameRuleCategory.CHAT)
            .buildAndRegister(Identifier.fromNamespaceAndPath(MOD_ID, "broadcast_macro_failure"));


    public static final GameRule<Boolean> LOG_MACRO_FAILURE = GameRuleBuilder
            .forBoolean(false)
            .category(GameRuleCategory.CHAT)
            .buildAndRegister(Identifier.fromNamespaceAndPath(MOD_ID, "log_macro_failure"));

    @Override
    public void onInitialize() {
    }

    public enum TriggerFeedback {
        disabled, sourceOnly, sourceAndOps
    }
}
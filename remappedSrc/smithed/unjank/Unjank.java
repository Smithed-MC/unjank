package smithed.unjank;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unjank implements ModInitializer {
    public static final String MOD_ID = "unjank";
    public static final smithed.unjank.UnjankConfig CONFIG = smithed.unjank.UnjankConfig.createAndLoad();

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final GameRules.Key<EnumRule<TriggerFeedback>> SEND_TRIGGER_FEEDBACK =
            GameRuleRegistry.register("sendTriggerFeedback", GameRules.Category.CHAT, GameRuleFactory.createEnumRule(TriggerFeedback.disabled));

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");
    }

    public enum TriggerFeedback {
        disabled,
        sourceOnly,
        sourceAndOps
    }
}
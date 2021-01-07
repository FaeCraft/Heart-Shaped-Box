package io.github.faecraft.heartshapedbox.main;

import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.HeadBodyPart;
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic;
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandlerDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Pair;

import static net.minecraft.server.command.CommandManager.literal;

public class HSBMain implements ModInitializer {
    @Override
    public void onInitialize() {
        DamageHandlerDispatcher.registerHandlers();
        
        ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> {
                // Update FlexBoxes
                try {
                    PlayerLookup.all(minecraftServer).forEach(HSBMiscLogic::updatePlayerFlexBoxes);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                // Debuff all players accordingly
                PlayerLookup.all(minecraftServer).forEach(HSBMiscLogic::debuffPlayer);
            }
        );
        
        // Debug command
        // TODO: REMOVE THIS! or add an op requirement idc
        CommandRegistrationCallback.EVENT.register((commandDispatcher, b) ->
            commandDispatcher.register(
                literal("hsb").executes(context -> {
                    ServerCommandSource source = context.getSource();
                    BodyPartProvider provider = (BodyPartProvider)context.getSource().getPlayer();
                    
                    source.sendFeedback(new LiteralText("Head"), false);
                    HeadBodyPart head = provider.getHead();
                    source.sendFeedback(new LiteralText("- CENTER: " + head.getHealth()), false);
                    
                    source.sendFeedback(new LiteralText("Arms"), false);
                    Pair<ArmBodyPart, ArmBodyPart> arms = provider.getArms();
                    source.sendFeedback(new LiteralText("- LEFT: " + arms.getLeft().getHealth()), false);
                    source.sendFeedback(new LiteralText("- RIGHT: " + arms.getRight().getHealth()), false);
                    
                    source.sendFeedback(new LiteralText("Legs"), false);
                    Pair<LegBodyPart, LegBodyPart> legs = provider.getLegs();
                    source.sendFeedback(new LiteralText("- LEFT: " + legs.getLeft().getHealth()), false);
                    source.sendFeedback(new LiteralText("- RIGHT: " + legs.getRight().getHealth()), false);
                    
                    source.sendFeedback(new LiteralText("Feet"), false);
                    Pair<FootBodyPart, FootBodyPart> feet = provider.getFeet();
                    source.sendFeedback(new LiteralText("- LEFT: " + feet.getLeft().getHealth()), false);
                    source.sendFeedback(new LiteralText("- RIGHT: " + feet.getRight().getHealth()), false);
                    return 1;
                })
                    .then(literal("reset").executes(context -> {
                        ServerCommandSource source = context.getSource();
                        BodyPartProvider provider = (BodyPartProvider)context.getSource().getPlayer();
                        
                        for (AbstractBodyPart limb : provider.getAll()) {
                            limb.setHealth(limb.getDefaultMaxHealth());
                        }
                        
                        source.sendFeedback(new LiteralText("Reset health"), false);
                        return 1;
                    }))
            ));
    }
}

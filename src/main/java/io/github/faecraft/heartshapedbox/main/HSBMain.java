package io.github.faecraft.heartshapedbox.main;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic;
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandlerDispatcher;
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraft.server.command.CommandManager.literal;

public class HSBMain implements ModInitializer {
    private static Logger LOGGER = LogManager.getLogger("HSBMain");
    public static final String MOD_ID = "heartshapedbox";

    @Override
    public void onInitialize() {
        DamageHandlerDispatcher.registerHandlers();

//        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
//            if (entity instanceof ServerPlayerEntity) {
//                LOGGER.info("ENTITY LOAD | Entity: " + entity + " World: " + world);
//
//                S2CBodyPartSyncPacket.from((ServerPlayerEntity) entity).send((ServerPlayerEntity) entity);
//            }
//        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, server) -> {
            LOGGER.info("AFTER RESPAWN | Old: " + oldPlayer + " New: " + newPlayer + " Server: " + server);

            S2CBodyPartSyncPacket.from(newPlayer).send(newPlayer);
        });

        ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> {
                    // Update FlexBoxes
                    PlayerLookup.all(minecraftServer).forEach(HSBMiscLogic::updatePlayerFlexBoxes);
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
                            BodyPartProvider provider = (BodyPartProvider) context.getSource().getPlayer();

                            for (AbstractBodyPart part : provider.getParts()) {
                                source.sendFeedback(
                                        new LiteralText(part.getIdentifier().toString())
                                                .append(new LiteralText(" - "))
                                                .append(new LiteralText(part.getHealth() + "/" + part.getMaxHealth())),
                                        false);
                            }

                            return 1;
                        })
                                .then(literal("reset").executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    BodyPartProvider provider = (BodyPartProvider) context.getSource().getPlayer();

                                    for (AbstractBodyPart limb : provider.getParts()) {
                                        limb.setHealth(limb.getDefaultMaxHealth());
                                    }

                                    player.setHealth(player.getMaxHealth());

                                    source.sendFeedback(new LiteralText("Reset health"), false);
                                    return 1;
                                }))
                ));
    }
}

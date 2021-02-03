package io.github.faecraft.heartshapedbox.main

import net.fabricmc.api.ModInitializer
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandlerDispatcher
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AfterRespawn
import net.minecraft.server.network.ServerPlayerEntity
import io.github.faecraft.heartshapedbox.main.HSBMain
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.command.CommandManager
import com.mojang.brigadier.context.CommandContext
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import net.minecraft.text.LiteralText
import org.apache.logging.log4j.LogManager
import java.util.function.Consumer

class HSBMain : ModInitializer {
    override fun onInitialize() {
        DamageHandlerDispatcher.registerHandlers()

//        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
//            if (entity instanceof ServerPlayerEntity) {
//                LOGGER.info("ENTITY LOAD | Entity: " + entity + " World: " + world);
//
//                S2CBodyPartSyncPacket.from((ServerPlayerEntity) entity).send((ServerPlayerEntity) entity);
//            }
//        });

        ServerPlayerEvents.AFTER_RESPAWN.register(AfterRespawn { oldPlayer: ServerPlayerEntity, newPlayer: ServerPlayerEntity, server: Boolean ->
            LOGGER.info(
                "AFTER RESPAWN | Old: $oldPlayer New: $newPlayer Server: $server"
            )
            S2CBodyPartSyncPacket.from(newPlayer).send(newPlayer)
        })
        
        ServerTickEvents.END_SERVER_TICK.register(
            ServerTickEvents.EndTick { minecraftServer: MinecraftServer? ->
                // Update FlexBoxes
                PlayerLookup.all(minecraftServer).forEach(Consumer { playerEntity: ServerPlayerEntity? ->
                    HSBMiscLogic.updatePlayerFlexBoxes(playerEntity)
                })
                // Debuff all players accordingly
                PlayerLookup.all(minecraftServer)
                    .forEach(Consumer { playerEntity: ServerPlayerEntity? -> HSBMiscLogic.debuffPlayer(playerEntity) })
            }
        )

        // Debug command
        // TODO: REMOVE THIS! or add an op requirement idc
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { commandDispatcher: CommandDispatcher<ServerCommandSource?>, b: Boolean ->
            commandDispatcher.register(
                CommandManager.literal("hsb").executes { context: CommandContext<ServerCommandSource> ->
                    val source = context.source
                    val provider = context.source.player as BodyPartProvider
                    for (part in provider.parts) {
                        source.sendFeedback(
                            LiteralText(part.identifier.toString())
                                .append(LiteralText(" - "))
                                .append(LiteralText(part.health.toString() + "/" + part.maxHealth)),
                            false
                        )
                    }
                    1
                }
                    .then(CommandManager.literal("reset").executes { context: CommandContext<ServerCommandSource> ->
                        val source = context.source
                        val player = context.source.player
                        val provider = context.source.player as BodyPartProvider
                        for (limb in provider.parts) {
                            limb.health = limb.defaultMaxHealth
                        }
                        player.health = player.maxHealth
                        source.sendFeedback(LiteralText("Reset health"), false)
                        1
                    })
            )
        })
    }

    companion object {
        private val LOGGER = LogManager.getLogger("HSBMain")
        const val MOD_ID = "heartshapedbox"
    }
}

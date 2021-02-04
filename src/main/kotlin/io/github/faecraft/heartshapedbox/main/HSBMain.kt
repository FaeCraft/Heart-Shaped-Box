package io.github.faecraft.heartshapedbox.main

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandlerDispatcher
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
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

        ServerPlayerEvents.AFTER_RESPAWN.register()
        { oldPlayer: ServerPlayerEntity, newPlayer: ServerPlayerEntity, server: Boolean ->
            LOGGER.info(
                "AFTER RESPAWN | Old: $oldPlayer New: $newPlayer Server: $server"
            )
            S2CBodyPartSyncPacket.from(newPlayer).send(newPlayer)
        }

        ServerTickEvents.END_SERVER_TICK.register()
        { minecraftServer: MinecraftServer? ->
            // Update FlexBoxes
            PlayerLookup.all(minecraftServer).forEach(Consumer { playerEntity: ServerPlayerEntity ->
                HSBMiscLogic.updatePlayerFlexBoxes(playerEntity)
            })
            // Debuff all players accordingly
            PlayerLookup.all(minecraftServer)
                .forEach(Consumer { playerEntity: ServerPlayerEntity -> HSBMiscLogic.debuffPlayer(playerEntity) })
        }

        // Debug command
        // TODO: REMOVE THIS! or add an op requirement idc
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { commandDispatcher: CommandDispatcher<ServerCommandSource?>, b: Boolean ->
            commandDispatcher.register(
                CommandManager
                    .literal("hsb").executes { context: CommandContext<ServerCommandSource> ->
                        val source = context.source
                        val provider = context.source.player as BodyPartProvider
                        for (part in provider.parts) {
                            source.sendFeedback(
                                LiteralText(part.getIdentifier().toString())
                                    .append(LiteralText(" - "))
                                    .append(LiteralText(part.getHealth().toString() + "/" + part.getMaxHealth())),
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
                            limb.setHealth(limb.getDefaultMaxHealth())
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

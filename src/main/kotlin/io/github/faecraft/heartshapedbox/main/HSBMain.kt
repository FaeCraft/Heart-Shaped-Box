package io.github.faecraft.heartshapedbox.main

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.effects.MorphineStatusEffect
import io.github.faecraft.heartshapedbox.items.HSBItems
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandlerDispatcher
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager

public class HSBMain : ModInitializer {
    override fun onInitialize() {
        DamageHandlerDispatcher.registerHandlers()

        // Status Effects
        Registry.register(Registry.STATUS_EFFECT, Identifier(MOD_ID, "morphine"), MORPHINE_STATUS_EFFECT)

        // Items
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "morphine"), HSBItems.MORPHINE)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "medkit"), HSBItems.MEDKIT)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "plaster"), HSBItems.PLASTER)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "heart_crystal"), HSBItems.HEART_CRYSTAL)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "heart_crystal_shard"), HSBItems.HEART_CRYSTAL_SHARD)

//        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
//            if (entity instanceof ServerPlayerEntity) {
//                LOGGER.info("ENTITY LOAD | Entity: " + entity + " World: " + world);
//
//                S2CBodyPartSyncPacket.from((ServerPlayerEntity) entity).send((ServerPlayerEntity) entity);
//            }
//        });

        ServerPlayerEvents.AFTER_RESPAWN.register(::afterRespawn)

        ServerTickEvents.END_SERVER_TICK.register { minecraftServer: MinecraftServer? ->
            // Update FlexBoxes
            PlayerLookup.all(minecraftServer).forEach { playerEntity: ServerPlayerEntity ->
                HSBMiscLogic.updatePlayerFlexBoxes(playerEntity)
            }

            // Debuff all players accordingly
            PlayerLookup.all(minecraftServer).forEach { playerEntity: ServerPlayerEntity ->
                HSBMiscLogic.debuffPlayer(playerEntity)
            }
        }

        // Debug command
        // TODO: REMOVE THIS! or add an op requirement idc
        CommandRegistrationCallback.EVENT.register(
            CommandRegistrationCallback { commandDispatcher: CommandDispatcher<ServerCommandSource?>, _: Boolean ->
                commandDispatcher.register(
                    CommandManager
                        .literal("hsb")
                        .executes { context: CommandContext<ServerCommandSource> ->
                            val source = context.source
                            val provider = context.source.player as BodyPartProvider
                            for (part in provider.parts) {
                                source.sendFeedback(
                                    LiteralText(part.getIdentifier().toString())
                                        .append(LiteralText(" - "))
                                        .append(
                                            LiteralText(
                                                part.getHealth().toString() + "/" + part.getMaxHealth()
                                            )
                                        ),
                                    false
                                )
                            }

                            1
                        }
                        .then(
                            CommandManager
                                .literal("reset")
                                .executes { context: CommandContext<ServerCommandSource> ->
                                    val source = context.source
                                    val player = context.source.player
                                    val provider = context.source.player as BodyPartProvider

                                    for (limb in provider.parts) {
                                        limb.setHealth(limb.getDefaultMaxHealth())
                                    }

                                    player.health = player.maxHealth
                                    source.sendFeedback(LiteralText("Reset health"), false)

                                    1
                                }
                        )
                )
            })
    }

    private fun afterRespawn(oldPlayer: ServerPlayerEntity, newPlayer: ServerPlayerEntity, server: Boolean) {
        // Extracted to a function because it's easier to understand with a signature this large

        LOGGER.info(
            "AFTER RESPAWN | Old: $oldPlayer New: $newPlayer Server: $server"
        )

        S2CBodyPartSyncPacket.from(newPlayer).send(newPlayer)
    }

    public companion object {
        private val LOGGER = LogManager.getLogger("HSBMain")
        public const val MOD_ID: String = "heartshapedbox"

        public val MORPHINE_STATUS_EFFECT: MorphineStatusEffect = MorphineStatusEffect()

        public val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.create(Identifier(MOD_ID, "items"))
            .icon { ItemStack(HSBItems.HEART_CRYSTAL) }
            .build()
    }
}

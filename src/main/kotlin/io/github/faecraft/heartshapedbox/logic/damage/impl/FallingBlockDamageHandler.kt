package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.constants.ArmorSlots
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

public class FallingBlockDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean = source.name == "anvil" || source.name == "fallingBlock"

    override fun getPossibleArmorPieces(player: ServerPlayerEntity): Iterable<ItemStack> =
        setOf(player.inventory.armor[ArmorSlots.HELMET])

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> = false to provider.getOrThrow(BuiltInParts.HEAD).takeDamage(amount)
}

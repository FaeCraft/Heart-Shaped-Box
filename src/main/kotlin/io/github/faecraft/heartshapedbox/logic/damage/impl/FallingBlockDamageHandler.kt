package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Pair

class FallingBlockDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean {
        return source!!.name == "anvil" || source.name == "fallingBlock"
    }

    override fun getPossibleArmorPieces(player: ServerPlayerEntity): Iterable<ItemStack> {
        // Helmet
        return setOf(player.inventory.armor[3])
    }

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> {
        return Pair(false, provider.getOrThrow(BuiltInParts.HEAD).takeDamage(amount))
    }
}

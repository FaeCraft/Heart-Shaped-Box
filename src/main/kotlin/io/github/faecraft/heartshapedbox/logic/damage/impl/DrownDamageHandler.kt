package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

public class DrownDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean = source.name == "drown"

    override fun getPossibleArmorPieces(source: DamageSource, player: ServerPlayerEntity)
    // Empty as it always bypasses armor
    : List<ItemStack> = emptyList()

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> {
        // TODO: Damage both head and body when body is added
        val head = provider.getOrThrow(BuiltInParts.HEAD)
        return false to head.takeDamage(amount)
    }
}

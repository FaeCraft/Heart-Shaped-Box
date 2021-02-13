package io.github.faecraft.heartshapedbox.logic.damage

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

public interface DamageHandler {
    public fun shouldHandle(source: DamageSource): Boolean

    public fun getPossibleArmorPieces(source: DamageSource, player: ServerPlayerEntity): List<ItemStack>

    public fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float>
}

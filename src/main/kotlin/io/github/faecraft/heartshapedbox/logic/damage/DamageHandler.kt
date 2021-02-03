package io.github.faecraft.heartshapedbox.logic.damage

import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.item.ItemStack
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import net.minecraft.util.Pair

interface DamageHandler {
    fun shouldHandle(source: DamageSource): Boolean
    fun getPossibleArmorPieces(player: ServerPlayerEntity): Iterable<ItemStack?>? {
        return player.armorItems
    }

    fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float>
}

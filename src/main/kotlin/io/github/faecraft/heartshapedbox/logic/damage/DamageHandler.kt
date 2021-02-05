package io.github.faecraft.heartshapedbox.logic.damage

import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.item.ItemStack
import io.github.faecraft.heartshapedbox.body.BodyPartProvider

public interface DamageHandler {
    public fun getPossibleArmorPieces(player: ServerPlayerEntity): Iterable<ItemStack?>? = player.armorItems

    public fun shouldHandle(source: DamageSource): Boolean

    public fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float>
}

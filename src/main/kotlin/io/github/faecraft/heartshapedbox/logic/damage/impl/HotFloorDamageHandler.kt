package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic.dealDamageToPair
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getFeet
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.item.ItemStack
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import net.minecraft.util.Pair

class HotFloorDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean {
        return source!!.name == "hotFloor"
    }

    override fun getPossibleArmorPieces(player: ServerPlayerEntity): Iterable<ItemStack?>? {
        // Boots
        return setOf(player.inventory.armor[0])
    }

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> {
        return Pair(
            false, dealDamageToPair(
                getFeet(
                    provider!!
                ), amount
            )
        )
    }
}

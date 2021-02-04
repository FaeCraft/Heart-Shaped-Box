package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getFeet
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getLegs
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic.dealDamageToPair
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Pair
import java.util.*

class FallDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean {
        return source.name == "fall"
    }

    override fun getPossibleArmorPieces(player: ServerPlayerEntity): Iterable<ItemStack> {
        val out = ArrayList<ItemStack>()
        // Boots
        out.add(player.inventory.armor[0])
        // Leggings
        out.add(player.inventory.armor[1])
        return out
    }

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> {
        return Pair(
            false, dealDamageToPair(
                getLegs(provider),
                dealDamageToPair(getFeet(provider), amount)
            )
        )
    }
}

package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Pair
import java.util.*

class GenericDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean {
        return true
    }

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> {
        var amount = amount
        val parts = provider!!.parts
        var remainingTries = 30f
        do {
            val randomPart = parts!![Random().nextInt(parts.size)]
            remainingTries--
            if (randomPart!!.getHealth() <= 0) {
                continue
            }
            val result = randomPart.takeDamage(amount)
            amount -= amount - result
        } while (amount > 0 && remainingTries > 0)
        return Pair(true, amount)
    }
}

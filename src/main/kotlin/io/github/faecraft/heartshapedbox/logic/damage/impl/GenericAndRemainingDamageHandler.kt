package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

private const val DEFAULT_RETRIES: Float = 30f

public class GenericAndRemainingDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean = true

    override fun getPossibleArmorPieces(source: DamageSource, player: ServerPlayerEntity): List<ItemStack> {
        val provider: BodyPartProvider = player as BodyPartProvider
        val out = mutableListOf<ItemStack>()
        provider.parts.forEach {
            out.addAll(it.getAffectingArmor(player))
        }
        return out
    }

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> {
        var mutAmount = amount
        val parts = provider.parts
        var remainingTries = DEFAULT_RETRIES

        do {
            val randomPart = parts[Random().nextInt(parts.size)]

            remainingTries--

            if (randomPart.getHealth() <= 0) {
                continue
            }

            val result = randomPart.takeDamage(mutAmount)

            mutAmount -= mutAmount - result
        } while (mutAmount > 0 && remainingTries > 0)

        return true to mutAmount
    }
}

package io.github.faecraft.heartshapedbox.logic.damage

import io.github.faecraft.heartshapedbox.bad.BadMixinAtomicFlags
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.logic.damage.impl.*
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.DamageUtil
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats
import java.util.*

object DamageHandlerDispatcher {
    private val handlers = ArrayList<DamageHandler>()
    @JvmStatic
    fun handleDamage(player: ServerPlayerEntity, source: DamageSource, amount: Float): Boolean {
        var amount = amount
        val provider = player as BodyPartProvider
        if (player.hasStatusEffect(StatusEffects.RESISTANCE) && source !== DamageSource.OUT_OF_WORLD) {
            val k = (player.getStatusEffect(StatusEffects.RESISTANCE)!!.amplifier + 1) * 5
            val j = 25 - k
            val f = amount * j.toFloat()
            val g = amount
            amount = Math.max(f / 25.0f, 0.0f)
            val h = g - amount
            if (h > 0.0f && h < 3.4028235E37f) {
                player.increaseStat(Stats.DAMAGE_RESISTED, Math.round(h * 10.0f))
            }
        }

        // Done to deal damage in a single go
        var collectedDamage = 0f
        // Saves state before damage handled
        val stateBefore = provider.writeToTag()
        for (possibleHandler in handlers) {
            if (possibleHandler.shouldHandle(source)) {
                val items = possibleHandler.getPossibleArmorPieces(player)
                val k = EnchantmentHelper.getProtectionAmount(items, source)
                if (k > 0) {
                    amount = DamageUtil.getInflictedDamage(amount, k.toFloat())
                }
                val result = possibleHandler.handleDamage(player, player as BodyPartProvider, source, amount)
                collectedDamage += amount - result.right
                amount = result.right
                if (result.left || result.right <= 0) {
                    break
                }
            }
        }
        // Try to deal damage, roll back if failed
        // see BadMixinAtomicFlags for the reason behind this
        BadMixinAtomicFlags.callSuperDamage.set(true)
        val didDealDamage = player.damage(source, collectedDamage)
        BadMixinAtomicFlags.callSuperDamage.set(false)

        // Revert the state if vanilla doesn't like our damage for whatever reason
        if (!didDealDamage) {
            provider.readFromTag(stateBefore)
        }
        return didDealDamage
    }

    fun registerHandlers() {
        // Lower indices get tested first (i.e higher priority)
        handlers.add(ProjectileDamageHandler())
        handlers.add(FallingBlockDamageHandler())
        handlers.add(FallDamageHandler())
        handlers.add(HotFloorDamageHandler())
        handlers.add(GenericDamageHandler())
    }
}
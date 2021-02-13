package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getFeet
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getLegs
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic.dealDamageToPair
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

public class FallDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean = source.name == "fall"

    override fun getPossibleArmorPieces(source: DamageSource, player: ServerPlayerEntity): List<ItemStack> {
        val out = ArrayList<ItemStack>()
        val provider: BodyPartProvider = player as BodyPartProvider

        val legs = getLegs(provider)
        val feet = getFeet(provider)
        out.addAll(legs.left.getAffectingArmor(player))
        out.addAll(legs.right.getAffectingArmor(player))
        out.addAll(feet.left.getAffectingArmor(player))
        out.addAll(feet.right.getAffectingArmor(player))

        return out.distinct()
    }

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> = false to dealDamageToPair(
        getLegs(provider),
        dealDamageToPair(getFeet(provider), amount)
    )
}

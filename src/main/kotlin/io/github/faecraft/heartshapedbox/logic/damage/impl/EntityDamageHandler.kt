package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import io.github.faecraft.heartshapedbox.math.Ray
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.EntityDamageSource
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

public class EntityDamageHandler: DamageHandler {
    private var cachedPart: AbstractBodyPart? = null

    override fun shouldHandle(source: DamageSource)
    : Boolean = source is EntityDamageSource && source.attacker != null && !source.isThorns

    override fun getPossibleArmorPieces(source: DamageSource, player: ServerPlayerEntity): List<ItemStack> {
        val provider: BodyPartProvider = player as BodyPartProvider

        // Can suppress because our predicate only fires if its not null
        val ray = Ray(
            source.attacker!!.pos.add(0.0, source.attacker!!.eyeY, 0.0),
            source.attacker!!.rotationVector
        )
        val possible = provider.parts

        possible.sortWith(Comparator.comparingDouble { part: AbstractBodyPart ->
            part.flexBox.roughCenter.distanceTo(
                ray.start.add(
                    ray.normalizedDirection
                )
            )
        })

        for (limb in possible) {
            if (ray.intersectsBox(limb.flexBox)) {
                cachedPart = limb
                return limb.getAffectingArmor(player)
            }
        }

        cachedPart = null
        return emptyList()
    }

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> = false to (cachedPart?.takeDamage(amount) ?: amount)
}

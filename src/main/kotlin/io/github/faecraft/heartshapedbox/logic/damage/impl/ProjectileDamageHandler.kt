package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import io.github.faecraft.heartshapedbox.math.Ray
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.ProjectileDamageSource
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

public class ProjectileDamageHandler : DamageHandler {
    private lateinit var cachedPart: AbstractBodyPart

    override fun shouldHandle(source: DamageSource): Boolean =
        source is ProjectileDamageSource && source.getAttacker() != null

    override fun getPossibleArmorPieces(source: DamageSource, player: ServerPlayerEntity): List<ItemStack> {
        val provider: BodyPartProvider = player as BodyPartProvider

        // Can suppress because our predicate only fires if its not null
        val ray = Ray(source.attacker!!.pos, source.attacker!!.velocity)
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
        return emptyList()
    }

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
        // Can safely use cached part because getPossibleArmorPieces is always called first
    ): Pair<Boolean, Float> = false to cachedPart.takeDamage(amount)
}

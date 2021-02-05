package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import io.github.faecraft.heartshapedbox.math.Ray
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.ProjectileDamageSource
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

public class ProjectileDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean =
        source is ProjectileDamageSource && source.getAttacker() != null

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> {
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
                return false to limb.takeDamage(amount)
            }
        }

        System.err.println("Failed to find collision")
        System.err.println(player.pos)
        System.err.println(ray.start)
        System.err.println(ray.normalizedDirection)

        return false to amount
    }
}

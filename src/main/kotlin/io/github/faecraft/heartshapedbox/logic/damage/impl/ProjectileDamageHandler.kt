package io.github.faecraft.heartshapedbox.logic.damage.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler
import io.github.faecraft.heartshapedbox.math.Ray
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.ProjectileDamageSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Pair
import java.util.*

class ProjectileDamageHandler : DamageHandler {
    override fun shouldHandle(source: DamageSource): Boolean {
        return source is ProjectileDamageSource && source.getAttacker() != null
    }

    override fun handleDamage(
        player: ServerPlayerEntity,
        provider: BodyPartProvider,
        source: DamageSource,
        amount: Float
    ): Pair<Boolean, Float> {
        // Can suppress because our predicate only fires if its not null
        val ray = Ray(source.attacker!!.pos, source.attacker!!.velocity)
        val possible = provider.parts
        possible.sortWith(Comparator.comparingDouble { o: AbstractBodyPart ->
            o.flexBox.roughCenter.distanceTo(
                ray.start.add(
                    ray.direction
                )
            )
        })
        for (limb in possible) {
            if (ray.intersectsBox(limb.flexBox)) {
                return Pair(false, limb.takeDamage(amount))
            }
        }
        System.err.println("Failed to find collision")
        System.err.println(player.pos)
        System.err.println(ray.start)
        System.err.println(ray.direction)
        return Pair(false, amount)
    }
}

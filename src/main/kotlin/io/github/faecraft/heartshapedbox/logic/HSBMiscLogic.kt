package io.github.faecraft.heartshapedbox.logic

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getArms
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getFeet
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getLegs
import io.github.faecraft.heartshapedbox.body.impl.HeadBodyPart
import io.github.faecraft.heartshapedbox.math.FlexBox
import io.github.faecraft.heartshapedbox.math.two_d.Line
import io.github.faecraft.heartshapedbox.math.two_d.Square
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Pair
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import java.util.*

object HSBMiscLogic {
    @JvmStatic
    fun forceHealthChangeToLimbs(newAmount: Float, provider: BodyPartProvider) {
        val allLimbs: ArrayList<AbstractBodyPart> = try {
            provider.parts
        } catch (err: NullPointerException) {
            return
        }
        var totalCurrent = 0f
        for (limb in allLimbs) {
            totalCurrent += limb.getHealth()
        }


        // all damage is passed through damage handler which force sync through math anyways, so only option is healing
        // Also big epsilon because im sick of this triggering because the health differs by 1e^-6 NO ONE CARES
        if (newAmount - totalCurrent > 0.001) {
            var healingPool = newAmount - totalCurrent
            val criticalLimbs = allLimbs.stream().filter { it?.isCritical ?: false }.iterator()
            val normalLimbs =
                allLimbs.stream().filter { abstractBodyPart: AbstractBodyPart? -> !abstractBodyPart!!.isCritical }
                    .iterator()
            while (criticalLimbs.hasNext() && healingPool > 0) {
                val critLimb = criticalLimbs.next()
                val missing = critLimb!!.getMaxHealth() - critLimb.getHealth()
                if (missing > 0) {
                    if (healingPool >= missing) {
                        critLimb.setHealth(critLimb.getMaxHealth())
                    } else {
                        critLimb.setHealth(critLimb.getHealth() + healingPool)
                    }
                    healingPool -= missing
                }
            }
            if (healingPool > 0) {
                while (normalLimbs.hasNext() && healingPool > 0) {
                    val limb = normalLimbs.next()
                    val missing = limb!!.getMaxHealth() - limb.getHealth()
                    if (missing > 0) {
                        if (healingPool >= missing) {
                            limb.setHealth(limb.getMaxHealth())
                        } else {
                            limb.setHealth(limb.getHealth() + healingPool)
                        }
                        healingPool -= missing
                    }
                }
            }
        }
    }

    fun updatePlayerFlexBoxes(playerEntity: ServerPlayerEntity) {
        val provider = playerEntity as BodyPartProvider
        val pos = playerEntity.pos
        val boundingBox = playerEntity.getBoundingBox(playerEntity.pose)
        val playerBoxSlice = Square(
            Vec2f((pos.x + boundingBox.minX).toFloat(), (pos.z + boundingBox.minZ).toFloat()),
            Vec2f((pos.x + boundingBox.maxX).toFloat(), (pos.z + boundingBox.maxZ).toFloat())
        )
        val facingLine = Line(
            Vec2f(pos.x.toFloat(), pos.z.toFloat()),
            (if (playerEntity.yaw == 0f) 0.00001f else playerEntity.yaw).toDouble()
        )
        val results = playerBoxSlice.splitFromLine(facingLine)
        val leftSet = results.first
        val rightSet = results.second

        // Feet
        provider.getOrThrow(BuiltInParts.LEFT_FOOT).flexBox = FlexBox(
            v3FromV2(leftSet[0], pos.y),
            v3FromV2(leftSet[1], pos.y),
            v3FromV2(leftSet[2], pos.y),
            v3FromV2(leftSet[3], pos.y),
            0.2
        )
        provider.getOrThrow(BuiltInParts.RIGHT_FOOT).flexBox = FlexBox(
            v3FromV2(rightSet[0], pos.y),
            v3FromV2(rightSet[1], pos.y),
            v3FromV2(rightSet[2], pos.y),
            v3FromV2(rightSet[3], pos.y),
            0.2
        )

        // Legs
        provider.getOrThrow(BuiltInParts.LEFT_LEG).flexBox = FlexBox(
            v3FromV2(leftSet[0], pos.y + 0.2),
            v3FromV2(leftSet[1], pos.y + 0.2),
            v3FromV2(leftSet[2], pos.y + 0.2),
            v3FromV2(leftSet[3], pos.y + 0.2),
            0.6
        )
        provider.getOrThrow(BuiltInParts.RIGHT_LEG).flexBox = FlexBox(
            v3FromV2(rightSet[0], pos.y + 0.2),
            v3FromV2(rightSet[1], pos.y + 0.2),
            v3FromV2(rightSet[2], pos.y + 0.2),
            v3FromV2(rightSet[3], pos.y + 0.2),
            0.6
        )

        // Arms
        provider.getOrThrow(BuiltInParts.LEFT_ARM).flexBox = FlexBox(
            v3FromV2(leftSet[0], pos.y + 0.2 + 0.6),
            v3FromV2(leftSet[1], pos.y + 0.2 + 0.6),
            v3FromV2(leftSet[2], pos.y + 0.2 + 0.6),
            v3FromV2(leftSet[3], pos.y + 0.2 + 0.6),
            0.8
        )
        provider.getOrThrow(BuiltInParts.RIGHT_ARM).flexBox = FlexBox(
            v3FromV2(rightSet[0], pos.y + 0.2 + 0.6),
            v3FromV2(rightSet[1], pos.y + 0.2 + 0.6),
            v3FromV2(rightSet[2], pos.y + 0.2 + 0.6),
            v3FromV2(rightSet[3], pos.y + 0.2 + 0.6),
            0.8
        )

        // Head
        provider.getOrThrow(BuiltInParts.HEAD).flexBox = FlexBox(
            v3FromV2(leftSet[2], pos.y + 0.2 + 0.6 + 0.8),
            v3FromV2(leftSet[1], pos.y + 0.2 + 0.6 + 0.8),
            v3FromV2(rightSet[1], pos.y + 0.2 + 0.6 + 0.8),
            v3FromV2(rightSet[2], pos.y + 0.2 + 0.6 + 0.8),
            0.4
        )
    }

    fun debuffPlayer(playerEntity: ServerPlayerEntity) {
        if (!playerEntity.isAlive) {
            return
        }
        val provider = playerEntity as BodyPartProvider

        // Check for broken legs/feet
        // Each broken part adds a slowness level
        val legs = getLegs(provider)
        val feet = getFeet(provider)
        var slowAmp = -1
        if (legs.left.getHealth() <= 0) slowAmp++
        if (legs.right.getHealth() <= 0) slowAmp++
        if (feet.left.getHealth() <= 0) slowAmp++
        if (feet.right.getHealth() <= 0) slowAmp++
        if (slowAmp > -1) {
            playerEntity.addStatusEffect(StatusEffectInstance(StatusEffects.SLOWNESS, 2, slowAmp, true, true))
        }

        // Check for broken arms
        // Each broken one adds a mining fatigue level
        val arms = getArms(provider)
        var fatigueAmp = -1
        if (arms.left.getHealth() <= 0) fatigueAmp++
        if (arms.right.getHealth() <= 0) fatigueAmp++
        if (fatigueAmp > -1) {
            playerEntity.addStatusEffect(StatusEffectInstance(StatusEffects.MINING_FATIGUE, 2, fatigueAmp, true, true))
        }

        // Blindness and nausea if low on health
        val head = provider.getOrThrow(BuiltInParts.HEAD) as HeadBodyPart?
        if (head!!.getHealth() <= 2) {
            // Causes rapid strobing at ~23->~10, be careful!
            playerEntity.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, 25, 0, true, true))
            // Has no effect at 61 or below
            playerEntity.addStatusEffect(StatusEffectInstance(StatusEffects.NAUSEA, 62, 0, true, true))
        }
        for (limb in provider.parts) {
            if (limb.isCritical && limb.getHealth() <= 0) {
                // Slightly hacky way to make em die with proper death message
                playerEntity.health = -1f
            }
        }
    }

    @JvmStatic
    fun <T : AbstractBodyPart?> dealDamageToPair(pair: Pair<T, T>, amount: Float): Float {
        val halved = amount / 2
        // Deal half to left
        // Deal left-over to right
        // return leftover
        return pair.right!!.takeDamage(
            halved +
                    pair.left!!.takeDamage(halved)
        )
    }

    private fun v3FromV2(vec: Vec2f, height: Double): Vec3d {
        return Vec3d(vec.x.toDouble(), height, vec.y.toDouble())
    }
}

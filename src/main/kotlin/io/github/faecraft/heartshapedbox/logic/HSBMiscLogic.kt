package io.github.faecraft.heartshapedbox.logic

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getArms
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getFeet
import io.github.faecraft.heartshapedbox.body.BuiltInParts.getLegs
import io.github.faecraft.heartshapedbox.main.HSBMain
import io.github.faecraft.heartshapedbox.math.FlexBox
import io.github.faecraft.heartshapedbox.math.twoD.Line
import io.github.faecraft.heartshapedbox.math.twoD.Square
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Pair
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import java.util.*

private const val MINING_FATIGUE_DURATION: Int = 2
private const val BLINDNESS_DURATION: Int = 25 // Causes rapid strobing at ~23->~10, be careful!
private const val NAUSEA_DURATION: Int = 62 // Has no effect at 61 or below

public object HSBMiscLogic {
    @JvmStatic
    public fun forceHealthChangeToLimbs(newAmount: Float, provider: BodyPartProvider) {
        val allLimbs: ArrayList<AbstractBodyPart> = try {
            provider.parts
        } catch (ignored: NullPointerException) {
            return
        }

        var totalCurrent = 0f
        for (limb in allLimbs) {
            totalCurrent += limb.getHealth()
        }

        // all damage is passed through damage handler which force sync through math anyways, so only option is healing
        // Also big epsilon because im sick of this triggering because the health differs by 1e^-6 NO ONE CARES
        @Suppress("MagicNumber")
        if (newAmount - totalCurrent > 0.001) {
            var healingPool = newAmount - totalCurrent
            val criticalLimbs = allLimbs.filter { it.isCritical }.iterator()
            val normalLimbs = allLimbs.filter { abstractBodyPart: AbstractBodyPart -> !abstractBodyPart.isCritical }

            for (critLimb in criticalLimbs) {
                if (healingPool <= 0) {
                    break
                }

                val missing = critLimb.getMaxHealth() - critLimb.getHealth()
                if (missing > 0) {
                    if (healingPool >= missing) {
                        critLimb.setHealth(critLimb.getMaxHealth())
                    } else {
                        critLimb.setHealth(critLimb.getHealth() + healingPool)
                    }
                    healingPool -= missing
                }
            }
            for (limb in normalLimbs) {
                if (healingPool <= 0) {
                    break
                }

                val missing = limb.getMaxHealth() - limb.getHealth()
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

    public fun updatePlayerFlexBoxes(playerEntity: ServerPlayerEntity) {
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

        val feetHeight = 0.2
        val legHeight = 0.6
        val armOrCenterHeight = 0.8
        val headHeight = 0.4

        // Feet
        provider.getOrThrow(BuiltInParts.LEFT_FOOT).flexBox = FlexBox(
            v3FromV2(leftSet[0], pos.y),
            v3FromV2(leftSet[1], pos.y),
            v3FromV2(leftSet[2], pos.y),
            v3FromV2(leftSet[3], pos.y),
            feetHeight
        )
        provider.getOrThrow(BuiltInParts.RIGHT_FOOT).flexBox = FlexBox(
            v3FromV2(rightSet[0], pos.y),
            v3FromV2(rightSet[1], pos.y),
            v3FromV2(rightSet[2], pos.y),
            v3FromV2(rightSet[3], pos.y),
            feetHeight
        )

        // Legs
        provider.getOrThrow(BuiltInParts.LEFT_LEG).flexBox = FlexBox(
            v3FromV2(leftSet[0], pos.y + feetHeight),
            v3FromV2(leftSet[1], pos.y + feetHeight),
            v3FromV2(leftSet[2], pos.y + feetHeight),
            v3FromV2(leftSet[3], pos.y + feetHeight),
            legHeight
        )
        provider.getOrThrow(BuiltInParts.RIGHT_LEG).flexBox = FlexBox(
            v3FromV2(rightSet[0], pos.y + feetHeight),
            v3FromV2(rightSet[1], pos.y + feetHeight),
            v3FromV2(rightSet[2], pos.y + feetHeight),
            v3FromV2(rightSet[3], pos.y + feetHeight),
            legHeight
        )

        // Arms
        provider.getOrThrow(BuiltInParts.LEFT_ARM).flexBox = FlexBox(
            v3FromV2(leftSet[0], pos.y + feetHeight + legHeight),
            v3FromV2(leftSet[1], pos.y + feetHeight + legHeight),
            v3FromV2(leftSet[2], pos.y + feetHeight + legHeight),
            v3FromV2(leftSet[3], pos.y + feetHeight + legHeight),
            armOrCenterHeight
        )
        provider.getOrThrow(BuiltInParts.RIGHT_ARM).flexBox = FlexBox(
            v3FromV2(rightSet[0], pos.y + feetHeight + legHeight),
            v3FromV2(rightSet[1], pos.y + feetHeight + legHeight),
            v3FromV2(rightSet[2], pos.y + feetHeight + legHeight),
            v3FromV2(rightSet[3], pos.y + feetHeight + legHeight),
            armOrCenterHeight
        )

        // Head
        provider.getOrThrow(BuiltInParts.HEAD).flexBox = FlexBox(
            v3FromV2(leftSet[2], pos.y + feetHeight + legHeight + armOrCenterHeight),
            v3FromV2(leftSet[1], pos.y + feetHeight + legHeight + armOrCenterHeight),
            v3FromV2(rightSet[1], pos.y + feetHeight + legHeight + armOrCenterHeight),
            v3FromV2(rightSet[2], pos.y + feetHeight + legHeight + armOrCenterHeight),
            headHeight
        )
    }

    public fun debuffPlayer(playerEntity: ServerPlayerEntity) {
        if (!playerEntity.isAlive) {
            return
        }

        val provider = playerEntity as BodyPartProvider
        for (limb in provider.parts) {
            if (limb.isCritical && limb.getHealth() <= 0) {
                // Slightly hacky way to make em die with proper death message
                playerEntity.health = -1f
            }
        }

        // Don't apply debuffs if the player has morphine effect
        if (playerEntity.hasStatusEffect(HSBMain.MORPHINE_STATUS_EFFECT)) {
            return
        }

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
            playerEntity.addStatusEffect(StatusEffectInstance(
                StatusEffects.MINING_FATIGUE, MINING_FATIGUE_DURATION, fatigueAmp, true, true)
            )
        }

        // Blindness and nausea if low on health
        val head = provider.getOrThrow(BuiltInParts.HEAD)

        if (head.getHealth() <= 2) {
            playerEntity.addStatusEffect(
                StatusEffectInstance(StatusEffects.BLINDNESS, BLINDNESS_DURATION, 0, true, true)
            )

            playerEntity.addStatusEffect(
                StatusEffectInstance(StatusEffects.NAUSEA, NAUSEA_DURATION, 0, true, true)
            )
        }
    }

    public fun <T : AbstractBodyPart> dealDamageToPair(pair: Pair<T, T>, amount: Float): Float {
        val halved = amount / 2

        // Deal half to left
        // Deal left-over to right
        // Return leftover
        return pair.right.takeDamage(
            halved +
                    pair.left.takeDamage(halved)
        )
    }

    private fun v3FromV2(vec: Vec2f, height: Double): Vec3d = Vec3d(vec.x.toDouble(), height, vec.y.toDouble())
}

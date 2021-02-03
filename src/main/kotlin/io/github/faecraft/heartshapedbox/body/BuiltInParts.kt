package io.github.faecraft.heartshapedbox.body

import io.github.faecraft.heartshapedbox.main.HSBMain
import io.github.faecraft.heartshapedbox.body.BodyPartSide
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart
import net.minecraft.util.Identifier
import net.minecraft.util.Pair

object BuiltInParts {
    // Head
    @JvmField
    val HEAD = Identifier(HSBMain.MOD_ID, "head")

    // Arms
    @JvmField
    val LEFT_ARM = Identifier(HSBMain.MOD_ID, "arm/" + BodyPartSide.LEFT.lowerName())
    @JvmField
    val RIGHT_ARM = Identifier(HSBMain.MOD_ID, "arm/" + BodyPartSide.RIGHT.lowerName())

    // Legs
    @JvmField
    val LEFT_LEG = Identifier(HSBMain.MOD_ID, "leg/" + BodyPartSide.LEFT.lowerName())
    @JvmField
    val RIGHT_LEG = Identifier(HSBMain.MOD_ID, "leg/" + BodyPartSide.RIGHT.lowerName())

    // Feet
    @JvmField
    val LEFT_FOOT = Identifier(HSBMain.MOD_ID, "foot/" + BodyPartSide.LEFT.lowerName())
    @JvmField
    val RIGHT_FOOT = Identifier(HSBMain.MOD_ID, "foot/" + BodyPartSide.RIGHT.lowerName())

    // Pairs
    @JvmStatic
    fun getArms(provider: BodyPartProvider): Pair<ArmBodyPart, ArmBodyPart> {
        return Pair(
            provider.getOrThrow(LEFT_ARM) as ArmBodyPart,
            provider.getOrThrow(RIGHT_ARM) as ArmBodyPart
        )
    }

    @JvmStatic
    fun getLegs(provider: BodyPartProvider): Pair<LegBodyPart, LegBodyPart> {
        return Pair(
            provider.getOrThrow(LEFT_LEG) as LegBodyPart,
            provider.getOrThrow(RIGHT_LEG) as LegBodyPart
        )
    }

    @JvmStatic
    fun getFeet(provider: BodyPartProvider): Pair<FootBodyPart, FootBodyPart> {
        return Pair(
            provider.getOrThrow(LEFT_FOOT) as FootBodyPart,
            provider.getOrThrow(RIGHT_FOOT) as FootBodyPart
        )
    }
}

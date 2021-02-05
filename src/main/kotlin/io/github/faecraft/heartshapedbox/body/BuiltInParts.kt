package io.github.faecraft.heartshapedbox.body

import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart
import io.github.faecraft.heartshapedbox.main.HSBMain
import net.minecraft.util.Identifier
import net.minecraft.util.Pair

public object BuiltInParts {
    // Head
    @JvmField
    public val HEAD: Identifier = Identifier(HSBMain.MOD_ID, "head")

    // Arms
    @JvmField
    public val LEFT_ARM: Identifier = Identifier(HSBMain.MOD_ID, "arm/${BodyPartSide.LEFT.lowerName()}")

    @JvmField
    public val RIGHT_ARM: Identifier = Identifier(HSBMain.MOD_ID, "arm/${BodyPartSide.RIGHT.lowerName()}")

    // Legs
    @JvmField
    public val LEFT_LEG: Identifier = Identifier(HSBMain.MOD_ID, "leg/${BodyPartSide.LEFT.lowerName()}")

    @JvmField
    public val RIGHT_LEG: Identifier = Identifier(HSBMain.MOD_ID, "leg/${BodyPartSide.RIGHT.lowerName()}")

    // Feet
    @JvmField
    public val LEFT_FOOT: Identifier = Identifier(HSBMain.MOD_ID, "foot/${BodyPartSide.LEFT.lowerName()}")

    @JvmField
    public val RIGHT_FOOT: Identifier = Identifier(HSBMain.MOD_ID, "foot/${BodyPartSide.RIGHT.lowerName()}")

    // Pairs
    public fun getArms(provider: BodyPartProvider): Pair<ArmBodyPart, ArmBodyPart> {
        return Pair(
            provider.getOrThrow(LEFT_ARM) as ArmBodyPart,
            provider.getOrThrow(RIGHT_ARM) as ArmBodyPart
        )
    }

    public fun getLegs(provider: BodyPartProvider): Pair<LegBodyPart, LegBodyPart> {
        return Pair(
            provider.getOrThrow(LEFT_LEG) as LegBodyPart,
            provider.getOrThrow(RIGHT_LEG) as LegBodyPart
        )
    }

    public fun getFeet(provider: BodyPartProvider): Pair<FootBodyPart, FootBodyPart> {
        return Pair(
            provider.getOrThrow(LEFT_FOOT) as FootBodyPart,
            provider.getOrThrow(RIGHT_FOOT) as FootBodyPart
        )
    }
}

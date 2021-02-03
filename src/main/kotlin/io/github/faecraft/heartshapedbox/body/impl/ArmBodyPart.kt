package io.github.faecraft.heartshapedbox.body.impl

import net.minecraft.entity.player.PlayerEntity
import io.github.faecraft.heartshapedbox.body.BodyPartSide
import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import net.minecraft.util.Identifier

class ArmBodyPart(owner: PlayerEntity, private val side: BodyPartSide) : AbstractBodyPart(owner) {
    private val identifier = if (side == BodyPartSide.LEFT) BuiltInParts.LEFT_ARM else BuiltInParts.RIGHT_ARM

    override fun getIdentifier(): Identifier {
        return identifier
    }

    override fun getSide(): BodyPartSide {
        return side
    }

    override fun getDefaultMaxHealth(): Float {
        return 3f
    }
}

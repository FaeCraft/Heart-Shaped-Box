package io.github.faecraft.heartshapedbox.body.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartSide
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier

public class ArmBodyPart(owner: PlayerEntity, private val side: BodyPartSide) : AbstractBodyPart(owner) {
    private val identifier = if (side == BodyPartSide.LEFT) BuiltInParts.LEFT_ARM else BuiltInParts.RIGHT_ARM

    override fun getDefaultMaxHealth(): Float = 3f
    override fun getIdentifier(): Identifier = identifier
    override fun getSide(): BodyPartSide = side
}

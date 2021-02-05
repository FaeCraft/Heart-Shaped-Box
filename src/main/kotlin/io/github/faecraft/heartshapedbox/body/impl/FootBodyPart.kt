package io.github.faecraft.heartshapedbox.body.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartSide
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier

public class FootBodyPart(owner: PlayerEntity, private val side: BodyPartSide) : AbstractBodyPart(owner) {
    private val identifier = if (side == BodyPartSide.LEFT) BuiltInParts.LEFT_FOOT else BuiltInParts.RIGHT_FOOT

    override fun getDefaultMaxHealth(): Float = 2f
    override fun getIdentifier(): Identifier = identifier
    override fun getSide(): BodyPartSide = side
}

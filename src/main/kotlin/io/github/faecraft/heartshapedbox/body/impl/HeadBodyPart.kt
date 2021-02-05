package io.github.faecraft.heartshapedbox.body.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartSide
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier

public class HeadBodyPart(owner: PlayerEntity) : AbstractBodyPart(owner) {
    override val isCritical: Boolean = true

    override fun getIdentifier(): Identifier = BuiltInParts.HEAD
    override fun getDefaultMaxHealth(): Float = 4f
    override fun getSide(): BodyPartSide = BodyPartSide.CENTER
}

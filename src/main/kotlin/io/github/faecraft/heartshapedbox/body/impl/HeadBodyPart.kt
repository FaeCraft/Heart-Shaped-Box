package io.github.faecraft.heartshapedbox.body.impl

import net.minecraft.entity.player.PlayerEntity
import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.body.BodyPartSide
import net.minecraft.util.Identifier

class HeadBodyPart(owner: PlayerEntity) : AbstractBodyPart(owner) {
    override val isCritical: Boolean = true

    override fun getIdentifier(): Identifier {
        return BuiltInParts.HEAD
    }

    override fun getSide(): BodyPartSide {
        return BodyPartSide.CENTER
    }

    override fun getDefaultMaxHealth(): Float {
        return 4f
    }
}

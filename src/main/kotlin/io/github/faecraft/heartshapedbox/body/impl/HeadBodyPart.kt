package io.github.faecraft.heartshapedbox.body.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartSide
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.constants.ArmorSlots
import io.github.faecraft.heartshapedbox.constants.LimbHeights
import io.github.faecraft.heartshapedbox.math.FlexBox
import io.github.faecraft.heartshapedbox.util.FlexboxBaseBuilder
import io.github.faecraft.heartshapedbox.util.QuadSame
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityPose.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f

public class HeadBodyPart(owner: PlayerEntity) : AbstractBodyPart(owner) {
    override val isCritical: Boolean = true

    override fun getIdentifier(): Identifier = BuiltInParts.HEAD
    override fun getSide(): BodyPartSide = BodyPartSide.CENTER
    override fun getDefaultMaxHealth(): Float = 4f
    override fun getAffectingArmor(player: ServerPlayerEntity)
            : List<ItemStack> = listOf(player.inventory.armor[ArmorSlots.HELMET])

    override fun generateFlexBox(
        player: ServerPlayerEntity,
        pose: EntityPose,
        leftSet: QuadSame<Vec2f>,
        rightSet: QuadSame<Vec2f>
    ): FlexBox {
        val pos = player.pos
        return when (pose) {
            STANDING -> FlexBox(
                FlexboxBaseBuilder(
                    pos.y + LimbHeights.FOOT_HEIGHT + LimbHeights.LEG_HEIGHT + LimbHeights.CENTER_HEIGHT,
                    leftSet, rightSet, FlexboxBaseBuilder.BuildType.FULL_BOX),
                LimbHeights.HEAD_HEIGHT
            )
            FALL_FLYING -> TODO()
            SLEEPING -> FlexBox(
                FlexboxBaseBuilder(
                    pos.y,
                    leftSet, rightSet, FlexboxBaseBuilder.BuildType.FULL_BOX),
                LimbHeights.HEAD_HEIGHT
            )
            SWIMMING -> FlexBox.ZERO
            SPIN_ATTACK -> TODO()
            CROUCHING -> FlexBox(
                FlexboxBaseBuilder(
                    pos.y + LimbHeights.FOOT_HEIGHT_C + LimbHeights.LEG_HEIGHT_C + LimbHeights.CENTER_HEIGHT_C,
                    leftSet, rightSet, FlexboxBaseBuilder.BuildType.FULL_BOX),
                LimbHeights.HEAD_HEIGHT_C
            )
            DYING -> FlexBox.ZERO
        }
    }
}

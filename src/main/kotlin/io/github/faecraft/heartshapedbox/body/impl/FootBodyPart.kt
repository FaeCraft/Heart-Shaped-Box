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
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f

public class FootBodyPart(owner: PlayerEntity, private val side: BodyPartSide) : AbstractBodyPart(owner) {
    private val identifier = if (side == BodyPartSide.LEFT) BuiltInParts.LEFT_FOOT else BuiltInParts.RIGHT_FOOT

    override fun getIdentifier(): Identifier = identifier
    override fun getSide(): BodyPartSide = side
    override fun getDefaultMaxHealth(): Float = 2f
    override fun getAffectingArmor(player: ServerPlayerEntity)
            : List<ItemStack> = listOf(player.inventory.armor[ArmorSlots.BOOTS])

    override fun generateFlexBox(
        player: ServerPlayerEntity,
        pose: EntityPose,
        leftSet: QuadSame<Vec2f>,
        rightSet: QuadSame<Vec2f>
    ): FlexBox {
        val pos = player.pos
        return when (pose) {
            EntityPose.STANDING -> FlexBox(
                FlexboxBaseBuilder(pos.y, leftSet, rightSet, FlexboxBaseBuilder.BuildType.ofSide(side)),
                LimbHeights.FOOT_HEIGHT
            )
            EntityPose.FALL_FLYING -> FlexBox.ZERO
            EntityPose.SLEEPING -> FlexBox.ZERO
            EntityPose.SWIMMING -> FlexBox.ZERO
            EntityPose.SPIN_ATTACK -> FlexBox.ZERO
            EntityPose.CROUCHING -> FlexBox(
                FlexboxBaseBuilder(pos.y, leftSet, rightSet, FlexboxBaseBuilder.BuildType.ofSide(side)),
                LimbHeights.FOOT_HEIGHT_C
            )
            EntityPose.DYING -> FlexBox.ZERO
        }
    }
}

package io.github.faecraft.heartshapedbox.body.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartSide
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.constants.ArmorSlots
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

public class LegBodyPart(owner: PlayerEntity, private val side: BodyPartSide) : AbstractBodyPart(owner) {
    private val identifier = if (side == BodyPartSide.LEFT) BuiltInParts.LEFT_LEG else BuiltInParts.RIGHT_LEG

    override fun getIdentifier(): Identifier = identifier
    override fun getSide(): BodyPartSide = side
    override fun getDefaultMaxHealth(): Float = 3f
    override fun getAffectingArmor(player: ServerPlayerEntity)
    : List<ItemStack> = listOf(player.inventory.armor[ArmorSlots.LEGGINGS])
}

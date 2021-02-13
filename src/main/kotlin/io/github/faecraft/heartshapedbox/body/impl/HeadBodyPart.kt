package io.github.faecraft.heartshapedbox.body.impl

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartSide
import io.github.faecraft.heartshapedbox.body.BuiltInParts
import io.github.faecraft.heartshapedbox.constants.ArmorSlots
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

public class HeadBodyPart(owner: PlayerEntity) : AbstractBodyPart(owner) {
    override val isCritical: Boolean = true

    override fun getIdentifier(): Identifier = BuiltInParts.HEAD
    override fun getSide(): BodyPartSide = BodyPartSide.CENTER
    override fun getDefaultMaxHealth(): Float = 4f
    override fun getAffectingArmor(player: ServerPlayerEntity)
    : List<ItemStack> = listOf(player.inventory.armor[ArmorSlots.HELMET])
}

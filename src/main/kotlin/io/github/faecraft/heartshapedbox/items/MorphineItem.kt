package io.github.faecraft.heartshapedbox.items

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

public class MorphineItem(settings: Settings) : Item(settings) {

    override fun use(
        world: World,
        user: PlayerEntity,
        hand: Hand
    ): TypedActionResult<ItemStack> = ItemUsage.consumeHeldItem(world, user, hand)

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BLOCK

    override fun getMaxUseTime(stack: ItemStack): Int = 30

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        println("Used morphine")
        if (user is PlayerEntity) {
            user.itemCooldownManager.set(stack.item, COOLDOWN_TIME)
        }
        return super.finishUsing(stack, world, user)
    }

    public companion object {
        public const val COOLDOWN_TIME: Int = 20
    }
}

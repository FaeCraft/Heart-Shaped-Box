package io.github.faecraft.heartshapedbox.items

import io.github.faecraft.heartshapedbox.main.HSBMain
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
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

    override fun getMaxUseTime(stack: ItemStack): Int = 27

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        user.addStatusEffect(StatusEffectInstance(HSBMain.MORPHINE_STATUS_EFFECT, MORPHINE_DURATION, 0))
        if (user is PlayerEntity) {
            user.itemCooldownManager.set(stack.item, COOLDOWN_TIME)
            if (!user.isCreative) {
                stack.decrement(1)
            }
        }
        return super.finishUsing(stack, world, user)
    }

    public companion object {
        public const val COOLDOWN_TIME: Int = 20
        public const val MORPHINE_DURATION: Int = 60 * 20 // 60 seconds
    }
}

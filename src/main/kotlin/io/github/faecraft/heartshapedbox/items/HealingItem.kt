package io.github.faecraft.heartshapedbox.items

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

public class HealingItem(
    public val healAmount: Float,
    public val healTimeTicks: Int,
    settings: Settings
) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        println("$user used $this which has a healAmount of $healAmount and takes $healTimeTicks ticks")
        return TypedActionResult.success(user.getStackInHand(hand))
    }
}

package io.github.faecraft.heartshapedbox.items

import io.github.faecraft.heartshapedbox.main.HSBMain
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item

public object HSBItems {
    private const val CHEAP_MAX_COUNT: Int = 8
    private const val EXPENSIVE_MAX_COUNT: Int = 4

    private const val HEAL_AMOUNT: Float = 5f
    private const val HEAL_TIME: Int = 15

    public val MORPHINE: Item = MorphineItem(
        FabricItemSettings().maxCount(EXPENSIVE_MAX_COUNT).group(HSBMain.ITEM_GROUP)
    )
    public val MEDKIT: Item = HealingItem(
        HEAL_AMOUNT * 2, HEAL_TIME * 2,
        FabricItemSettings().maxCount(EXPENSIVE_MAX_COUNT).group(HSBMain.ITEM_GROUP)
    )
    public val PLASTER: Item = HealingItem(
        HEAL_AMOUNT, HEAL_TIME,
        FabricItemSettings().maxCount(CHEAP_MAX_COUNT).group(HSBMain.ITEM_GROUP)
    )
    public val HEART_CRYSTAL: Item = Item(FabricItemSettings().maxCount(1).group(HSBMain.ITEM_GROUP))
    public val HEART_CRYSTAL_SHARD: Item = Item(FabricItemSettings().group(HSBMain.ITEM_GROUP))
}

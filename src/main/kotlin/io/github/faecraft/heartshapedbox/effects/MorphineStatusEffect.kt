package io.github.faecraft.heartshapedbox.effects

import io.github.faecraft.heartshapedbox.main.HSBMain
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType

public class MorphineStatusEffect : StatusEffect(StatusEffectType.BENEFICIAL, EFFECT_COLOR) {
    override fun getTranslationKey(): String = HSBMain.MOD_ID + ".effect.morphine"

    public companion object {
        public const val EFFECT_COLOR: Int = 0x8b5f65
    }
}

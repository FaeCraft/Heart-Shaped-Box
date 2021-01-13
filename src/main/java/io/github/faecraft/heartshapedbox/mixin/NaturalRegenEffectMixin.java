package io.github.faecraft.heartshapedbox.mixin;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HungerManager.class)
public class NaturalRegenEffectMixin {
    @ModifyVariable(method = "update", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    public boolean overrideNaturalRegenerationEffect(boolean original) {
        return false;
    }
}

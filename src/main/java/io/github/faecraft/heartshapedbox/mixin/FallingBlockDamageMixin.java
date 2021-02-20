package io.github.faecraft.heartshapedbox.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class FallingBlockDamageMixin {
    /**
     * Disables helmets protecting from falling block damage
     */
    @SuppressWarnings("SameReturnValue")
    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    public boolean disableFallingBlockDamageLogic(ItemStack itemStack) {
        return true;
    }
}

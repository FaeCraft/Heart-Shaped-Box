package io.github.faecraft.heartshapedbox.mixin.damage_invoke;

import io.github.faecraft.heartshapedbox.bad.BadMixinAtomicFlags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityDamageBypass extends LivingEntity {
    protected PlayerEntityDamageBypass(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void sendToPlayerEntitySuperDamageCall(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (BadMixinAtomicFlags.callSuperDamage.get()) {
            cir.setReturnValue(super.damage(source, amount));
        }
    }
}

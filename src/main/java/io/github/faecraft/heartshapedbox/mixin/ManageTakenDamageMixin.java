package io.github.faecraft.heartshapedbox.mixin;

import io.github.faecraft.heartshapedbox.logic.damage.DamageHandlerDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class ManageTakenDamageMixin extends LivingEntity {
    protected ManageTakenDamageMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    public boolean redirectDamageToCustomLogic(LivingEntity livingEntity, DamageSource source, float amount) {
        if (!this.world.isClient) {
            return DamageHandlerDispatcher.handleDamage((ServerPlayerEntity)(Object)this, source, amount);
        }
        return false;
    }
    
    @Redirect(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
    public float disableDefaultArmorEffectOnDamage(PlayerEntity playerEntity, DamageSource source, float amount) {
        return amount;
    }
    
    @Redirect(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;applyEnchantmentsToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
    public float disableDefaultEnchantmentEffectOnDamage(PlayerEntity playerEntity, DamageSource source, float amount) {
        return amount;
    }
}

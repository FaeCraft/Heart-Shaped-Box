package io.github.faecraft.heartshapedbox.mixin;

import io.github.faecraft.heartshapedbox.bad.BadMixinAtomicFlags;
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandlerDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class ManageTakenDamageMixin extends LivingEntity {
    @Shadow public abstract boolean damage(DamageSource source, float amount);
    
    protected ManageTakenDamageMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    public boolean redirectDamageToCustomLogic(LivingEntity livingEntity, DamageSource source, float amount) {
        if (BadMixinAtomicFlags.doNotPassDamageToCustomLogic.get()) {
            return super.damage(source, amount);
        }
        
        if (!this.world.isClient && !(source.isOutOfWorld() && amount == Float.MAX_VALUE)) {
            return DamageHandlerDispatcher.handleDamage((ServerPlayerEntity)(Object)this, source, amount);
        } else if (source.isOutOfWorld() && amount == Float.MAX_VALUE) {
            BadMixinAtomicFlags.doNotPassDamageToCustomLogic.set(true);
            damage(source, amount);
            BadMixinAtomicFlags.doNotPassDamageToCustomLogic.set(false);
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

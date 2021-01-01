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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class HandleTakenDamageMixin extends LivingEntity {
    protected HandleTakenDamageMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Inject(method = "applyDamage", at = @At("HEAD"))
    public void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        if (!this.world.isClient) {
            DamageHandlerDispatcher.handleDamage((ServerPlayerEntity)(Object)this, source, amount);
        }
    }
}

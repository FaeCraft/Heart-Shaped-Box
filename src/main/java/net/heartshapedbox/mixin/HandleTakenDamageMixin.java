package net.heartshapedbox.mixin;

import net.heartshapedbox.logic.damage.DamageHandlerDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerEntity.class)
public abstract class HandleTakenDamageMixin extends LivingEntity {
    protected HandleTakenDamageMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    /**
     * @author P03W
     *
     * The entirety of the damage logic is replaced
     * better to fail fast than fail silently
     */
    @Overwrite
    public void applyDamage(DamageSource source, float amount) {
        if (!this.world.isClient) {
            DamageHandlerDispatcher.handleDamage((ServerPlayerEntity)(Object)this, source, amount);
        }
    }
}

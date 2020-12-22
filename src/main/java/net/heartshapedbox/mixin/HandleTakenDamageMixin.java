package net.heartshapedbox.mixin;

import net.heartshapedbox.body.BodyPartProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class HandleTakenDamageMixin extends LivingEntity {
    private boolean isAlreadyProcessedDamage = false;
    
    @Shadow public abstract boolean damage(DamageSource source, float amount);
    
    protected HandleTakenDamageMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Inject(method = "damage", at = @At(value = "TAIL", shift = At.Shift.BY, by = -1), cancellable = true)
    public void applyDamageToIndividualBodyPart(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (isAlreadyProcessedDamage) {
            isAlreadyProcessedDamage = false;
            return;
        }
        isAlreadyProcessedDamage = true;
        if (source.name.equals("fall")) {
            // This would be recursive, hence the isAlreadyProcessedDamage flag
            damage(source, ((BodyPartProvider)this).getLegs().takeDamage(amount));
        }
        
        // Return true always because when mod is done we can safely redirect all damage into custom logic
        cir.setReturnValue(true);
    }
}

package net.heartshapedbox.logic.damage.impl;

import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.logic.damage.DamageHandler;
import net.heartshapedbox.math.Ray;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ProjectileDamageHandler implements DamageHandler {
    @Override
    public void handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        // Can suppress because our predicate only fires if its not null
        //noinspection ConstantConditions
        Ray ray = new Ray(source.getAttacker().getPos(), source.getAttacker().getVelocity());
        
        // Head
        if (ray.intersectsBox(provider.getHead().getFlexBox())) {
            provider.getHead().takeDamage(amount);
            return;
        }
    
        // Arms
        if (ray.intersectsBox(provider.getArms().getLeft().getFlexBox())) {
            provider.getArms().getLeft().takeDamage(amount);
            return;
        }
        if (ray.intersectsBox(provider.getArms().getRight().getFlexBox())) {
            provider.getArms().getRight().takeDamage(amount);
            return;
        }
    
        // Legs
        if (ray.intersectsBox(provider.getLegs().getLeft().getFlexBox())) {
            provider.getLegs().getLeft().takeDamage(amount);
            return;
        }
        if (ray.intersectsBox(provider.getLegs().getRight().getFlexBox())) {
            provider.getLegs().getRight().takeDamage(amount);
            return;
        }
    
        // Feet
        if (ray.intersectsBox(provider.getFeet().getLeft().getFlexBox())) {
            provider.getFeet().getLeft().takeDamage(amount);
            return;
        }
        if (ray.intersectsBox(provider.getFeet().getRight().getFlexBox())) {
            provider.getFeet().getRight().takeDamage(amount);
        }
    }
}

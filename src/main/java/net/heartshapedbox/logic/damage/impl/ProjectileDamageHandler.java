package net.heartshapedbox.logic.damage.impl;

import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.logic.damage.DamageHandler;
import net.heartshapedbox.math.Ray;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ProjectileDamageHandler implements DamageHandler {
    @Override
    public boolean shouldHandle(DamageSource source) {
        return source instanceof ProjectileDamageSource && source.getAttacker() != null;
    }
    
    @Override
    public float handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        // Can suppress because our predicate only fires if its not null
        //noinspection ConstantConditions
        Ray ray = new Ray(source.getAttacker().getPos(), source.getAttacker().getVelocity());
        
        // Head
        if (ray.intersectsBox(provider.getHead().getFlexBox())) {
            return provider.getHead().takeDamage(amount);
        }
    
        // Arms
        if (ray.intersectsBox(provider.getArms().getLeft().getFlexBox())) {
            return provider.getArms().getLeft().takeDamage(amount);
        }
        if (ray.intersectsBox(provider.getArms().getRight().getFlexBox())) {
            return provider.getArms().getRight().takeDamage(amount);
        }
    
        // Legs
        if (ray.intersectsBox(provider.getLegs().getLeft().getFlexBox())) {
            return provider.getLegs().getLeft().takeDamage(amount);
        }
        if (ray.intersectsBox(provider.getLegs().getRight().getFlexBox())) {
            return provider.getLegs().getRight().takeDamage(amount);
        }
    
        // Feet
        if (ray.intersectsBox(provider.getFeet().getLeft().getFlexBox())) {
            return provider.getFeet().getLeft().takeDamage(amount);
        }
        if (ray.intersectsBox(provider.getFeet().getRight().getFlexBox())) {
            return provider.getFeet().getRight().takeDamage(amount);
        }
        
        System.out.println("Failed to find collision");
        return amount;
    }
}

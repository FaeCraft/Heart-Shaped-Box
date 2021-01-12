package io.github.faecraft.heartshapedbox.logic.damage.impl;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler;
import io.github.faecraft.heartshapedbox.math.Ray;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;

public class ProjectileDamageHandler implements DamageHandler {
    @Override
    public boolean shouldHandle(DamageSource source) {
        return source instanceof ProjectileDamageSource && source.getAttacker() != null;
    }
    
    @Override
    public Pair<Boolean, Float> handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        // Can suppress because our predicate only fires if its not null
        //noinspection ConstantConditions
        Ray ray = new Ray(source.getAttacker().getPos(), source.getAttacker().getVelocity());
    
        ArrayList<AbstractBodyPart> possible = provider.getParts();
        possible.sort(Comparator.comparingDouble(o -> o.getFlexBox().roughCenter.distanceTo(ray.start.add(ray.direction))));
        
        for (AbstractBodyPart limb : possible) {
            if (ray.intersectsBox(limb.getFlexBox())) {
                return new Pair<>(false, limb.takeDamage(amount));
            }
        }
        
        System.err.println("Failed to find collision");
        System.err.println(player.getPos());
        System.err.println(ray.start);
        System.err.println(ray.direction);
        return new Pair<>(false, amount);
    }
}

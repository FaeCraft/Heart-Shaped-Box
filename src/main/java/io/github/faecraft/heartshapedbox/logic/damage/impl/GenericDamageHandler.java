package io.github.faecraft.heartshapedbox.logic.damage.impl;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class GenericDamageHandler implements DamageHandler {
    @Override
    public boolean shouldHandle(DamageSource source) {
        return true;
    }

    @Override
    public Pair<Boolean, Float> handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        ArrayList<AbstractBodyPart> parts = provider.getParts();

        float remainingTries = 30;
        do {
            AbstractBodyPart randomPart = parts.get(new Random().nextInt(parts.size()));
    
            remainingTries--;
            
            if (randomPart.getHealth() <= 0) {
                continue;
            }

            float result = randomPart.takeDamage(amount);
            amount -= amount - result;
        } while (amount > 0 && remainingTries > 0);
        return new Pair<>(true, amount);
    }
}

package net.heartshapedbox.logic.damage.impl;

import net.heartshapedbox.body.AbstractBodyPart;
import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.logic.damage.DamageHandler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Random;

public class GenericDamageHandler implements DamageHandler {
    @Override
    public boolean shouldHandle(DamageSource source) {
        return true;
    }
    
    @Override
    public float handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        ArrayList<AbstractBodyPart> parts = provider.getAll();
        
        float dealt;
        float cap = 30;
        do {
            AbstractBodyPart randomPart = parts.get(new Random().nextInt(parts.size()));
            dealt = amount - randomPart.takeDamage(amount);
            if (dealt > 0) {
                player.damage(source, dealt);
                amount -= dealt;
            }
            cap--;
        } while (amount > 0 && cap > 0);
        return dealt;
    }
}

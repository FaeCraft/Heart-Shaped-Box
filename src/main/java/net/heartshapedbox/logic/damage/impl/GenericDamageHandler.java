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
    public void handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        ArrayList<AbstractBodyPart> parts = new ArrayList<>();
        parts.add(provider.getHead());
        parts.add(provider.getArms().getLeft());
        parts.add(provider.getArms().getRight());
        parts.add(provider.getLegs().getLeft());
        parts.add(provider.getLegs().getRight());
        parts.add(provider.getFeet().getLeft());
        parts.add(provider.getFeet().getRight());
    
        float dealt;
        float cap = 30;
        do {
            AbstractBodyPart randomPart = parts.get(new Random().nextInt(parts.size()));
            dealt = amount - randomPart.takeDamage(amount);
            System.out.println(dealt);
            System.out.println(amount);
            if (dealt > 0) {
                System.out.println(player.damage(source, dealt));
                amount -= dealt;
            }
            cap--;
        } while (amount > 0 && cap > 0);
    }
}

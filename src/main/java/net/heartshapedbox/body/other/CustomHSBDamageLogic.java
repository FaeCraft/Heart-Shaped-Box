package net.heartshapedbox.body.other;

import net.heartshapedbox.body.AbstractBodyPart;
import net.heartshapedbox.body.BodyPartProvider;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class CustomHSBDamageLogic {
    public static void handleDamage(DamageSource source, ServerPlayerEntity playerEntity, float amount) {
        BodyPartProvider provider = (BodyPartProvider)playerEntity;
        
        if (source.name.equals("fall")) {
            // Deals damage to feet first, then legs
            // Currently extra damage is voided
            playerEntity.damage(
                source,
                Math.abs(
                    dealDamageToPair(
                        provider.getLegs(),
                        dealDamageToPair(provider.getFeet(), amount)
                    ) - amount
                )
            );
            return;
        }
        
        // No source, choose randomly
        if (source.getAttacker() == null) {
            ArrayList<AbstractBodyPart> parts = new ArrayList<>();
            parts.add(provider.getLegs().getLeft());
            parts.add(provider.getLegs().getRight());
            parts.add(provider.getFeet().getLeft());
            parts.add(provider.getFeet().getRight());
            
            AbstractBodyPart randomPart = parts.get(new Random().nextInt(parts.size()));
            randomPart.takeDamage(amount);
        }
    }
    
    private static <T extends AbstractBodyPart> float dealDamageToPair(Pair<T, T> pair, float amount) {
        float halved = amount / 2;
        // Deal half to left
        // Deal left-over to right
        // return leftover
        return pair.getRight().takeDamage(
            halved +
                pair.getLeft().takeDamage(halved)
        );
    }
}

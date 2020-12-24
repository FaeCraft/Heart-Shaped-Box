package net.heartshapedbox;

import net.heartshapedbox.body.AbstractBodyPart;
import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.body.impl.ArmBodyPart;
import net.heartshapedbox.body.impl.FootBodyPart;
import net.heartshapedbox.body.impl.HeadBodyPart;
import net.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Random;

public class HSBMiscLogic {
    public static void updatePlayerFlexBoxes(ServerPlayerEntity playerEntity) {
        BodyPartProvider provider = (BodyPartProvider)playerEntity;
        
        Vec3d pos = playerEntity.getPos();
    }
    
    public static void debuffPlayer(ServerPlayerEntity playerEntity) {
        BodyPartProvider provider = (BodyPartProvider)playerEntity;
        
        // Check for broken legs/feet
        // Each broken part adds a slowness level
        Pair<LegBodyPart, LegBodyPart> legs = provider.getLegs();
        Pair<FootBodyPart, FootBodyPart> feet = provider.getFeet();
        int slowAmp = -1;
        
        if (legs.getLeft().getHealth() <= 0) slowAmp++;
        if (legs.getRight().getHealth() <= 0) slowAmp++;
        if (feet.getLeft().getHealth() <= 0) slowAmp++;
        if (feet.getLeft().getHealth() <= 0) slowAmp++;
        
        if (slowAmp > -1) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, slowAmp, true, true));
        }
        
        // Check for broken arms
        // Each broken one adds a mining fatigue level
        Pair<ArmBodyPart, ArmBodyPart> arms = provider.getArms();
        int fatigueAmp = -1;
        if (arms.getLeft().getHealth() <= 0) fatigueAmp++;
        if (arms.getRight().getHealth() <= 0) fatigueAmp++;
        if (fatigueAmp > -1) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 2, fatigueAmp, true, true));
        }
        
        // Check for broken(? is it broken or just injured lol) head
        // Blindness and nausea if broken
        HeadBodyPart head = provider.getHead();
        if (head.getHealth() <= 0) {
            // Causes rapid strobing at ~23->~10, be careful!
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 25, 0, true, true));
            // Has no effect at 61 or below
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 62, 0, true, true));
        }
    }
    
    public static void handleDamage(DamageSource source, ServerPlayerEntity playerEntity, float amount) {
        BodyPartProvider provider = (BodyPartProvider)playerEntity;
        
        if (source.name.equals("fall")) {
            // Damage from fall
            // Deals damage to feet first, then legs
            playerEntity.damage(
                source,
                amount - dealDamageToPair(
                    provider.getLegs(),
                    dealDamageToPair(provider.getFeet(), amount)
                )
            );
            return;
        }
        
        if (source.name.equals("anvil") || source.name.equals("fallingBlock")) {
            // Damage from falling block entity
            playerEntity.damage(source, amount - provider.getHead().takeDamage(amount));
            return;
        }
        
        if (source.name.equals("hotFloor")) {
            // Magma + similar
            playerEntity.damage(source, amount - dealDamageToPair(provider.getFeet(), amount));
            return;
        }
        
        // No source, choose randomly
        // Keep dealing damage until its all used up
        if (source.getAttacker() == null) {
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
                playerEntity.damage(source, dealt);
                amount -= dealt;
                cap--;
            } while (amount > 0 && cap > 0);
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

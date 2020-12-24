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
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 2, slowAmp, true, true));
        }
        
        // Check for broken(? is it broken or just injured lol) head
        // Blindness and nausea if broken
        HeadBodyPart head = provider.getHead();
        if (head.getHealth() <= 0) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 2, 0, true, true));
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 2, 0, true, true));
        }
    }
    
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
            parts.add(provider.getHead());
            parts.add(provider.getArms().getLeft());
            parts.add(provider.getArms().getRight());
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

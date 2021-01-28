package io.github.faecraft.heartshapedbox.logic;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.body.BuiltInParts;
import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.HeadBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart;
import io.github.faecraft.heartshapedbox.math.FlexBox;
import io.github.faecraft.heartshapedbox.math.two_d.Line;
import io.github.faecraft.heartshapedbox.math.two_d.Square;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;

public class HSBMiscLogic {
    public static void forceHealthChangeToLimbs(float newAmount, BodyPartProvider provider) {
        ArrayList<AbstractBodyPart> allLimbs;
        try {
            allLimbs = provider.getParts();
        } catch (NullPointerException err) {
            return;
        }
        float totalCurrent = 0;
        for (AbstractBodyPart limb : allLimbs) {
            totalCurrent += limb.getHealth();
        }
    
    
        // all damage is passed through damage handler which force sync through math anyways, so only option is healing
        // Also big epsilon because im sick of this triggering because the health differs by 1e^-6 NO ONE CARES
        if (newAmount - totalCurrent > 0.001) {
            float healingPool = newAmount - totalCurrent;
            Iterator<AbstractBodyPart> criticalLimbs = allLimbs.stream().filter(AbstractBodyPart::isCritical).iterator();
            Iterator<AbstractBodyPart> normalLimbs = allLimbs.stream().filter(abstractBodyPart -> !abstractBodyPart.isCritical()).iterator();
    
            while (criticalLimbs.hasNext() && healingPool > 0) {
                AbstractBodyPart critLimb = criticalLimbs.next();
                float missing = critLimb.getMaxHealth() - critLimb.getHealth();
                if (missing > 0) {
                    if (healingPool >= missing) {
                        critLimb.setHealth(critLimb.getMaxHealth());
                    } else {
                        critLimb.setHealth(critLimb.getHealth() + healingPool);
                    }
                    healingPool -= missing;
                }
            }
            if (healingPool > 0) {
                while (normalLimbs.hasNext() && healingPool > 0) {
                    AbstractBodyPart limb = normalLimbs.next();
                    float missing = limb.getMaxHealth() - limb.getHealth();
                    if (missing > 0) {
                        if (healingPool >= missing) {
                            limb.setHealth(limb.getMaxHealth());
                        } else {
                            limb.setHealth(limb.getHealth() + healingPool);
                        }
                        healingPool -= missing;
                    }
                }
            }
        }
    }
    
    public static void updatePlayerFlexBoxes(ServerPlayerEntity playerEntity) {
        BodyPartProvider provider = (BodyPartProvider)playerEntity;
        
        Vec3d pos = playerEntity.getPos();
        Box boundingBox = playerEntity.getBoundingBox(playerEntity.getPose());
        
        Square playerBoxSlice = new Square(
            new Vec2f((float)(pos.x + boundingBox.minX), (float)(pos.z + boundingBox.minZ)),
            new Vec2f((float)(pos.x + boundingBox.maxX), (float)(pos.z + boundingBox.maxZ))
        );
        Line facingLine = new Line(
            new Vec2f((float)pos.x, (float)pos.z),
            playerEntity.yaw == 0 ? 0.00001 : playerEntity.yaw
        );
        
        Pair<Vec2f[], Vec2f[]> results = playerBoxSlice.splitFromLine(facingLine);
        Vec2f[] leftSet = results.getLeft();
        Vec2f[] rightSet = results.getRight();
        
        // Feet
        provider.getOrThrow(BuiltInParts.LEFT_FOOT).setFlexBox(new FlexBox(
            v3FromV2(leftSet[0], pos.y),
            v3FromV2(leftSet[1], pos.y),
            v3FromV2(leftSet[2], pos.y),
            v3FromV2(leftSet[3], pos.y),
            0.2
        ));
        provider.getOrThrow(BuiltInParts.RIGHT_FOOT).setFlexBox(new FlexBox(
            v3FromV2(rightSet[0], pos.y),
            v3FromV2(rightSet[1], pos.y),
            v3FromV2(rightSet[2], pos.y),
            v3FromV2(rightSet[3], pos.y),
            0.2
        ));
    
        // Legs
        provider.getOrThrow(BuiltInParts.LEFT_LEG).setFlexBox(new FlexBox(
            v3FromV2(leftSet[0], pos.y + 0.2),
            v3FromV2(leftSet[1], pos.y + 0.2),
            v3FromV2(leftSet[2], pos.y + 0.2),
            v3FromV2(leftSet[3], pos.y + 0.2),
            0.6
        ));
        provider.getOrThrow(BuiltInParts.RIGHT_LEG).setFlexBox(new FlexBox(
            v3FromV2(rightSet[0], pos.y + 0.2),
            v3FromV2(rightSet[1], pos.y + 0.2),
            v3FromV2(rightSet[2], pos.y + 0.2),
            v3FromV2(rightSet[3], pos.y + 0.2),
            0.6
        ));
    
        // Arms
        provider.getOrThrow(BuiltInParts.LEFT_ARM).setFlexBox(new FlexBox(
            v3FromV2(leftSet[0], pos.y + 0.2 + 0.6),
            v3FromV2(leftSet[1], pos.y + 0.2 + 0.6),
            v3FromV2(leftSet[2], pos.y + 0.2 + 0.6),
            v3FromV2(leftSet[3], pos.y + 0.2 + 0.6),
            0.8
        ));
        provider.getOrThrow(BuiltInParts.RIGHT_ARM).setFlexBox(new FlexBox(
            v3FromV2(rightSet[0], pos.y + 0.2 + 0.6),
            v3FromV2(rightSet[1], pos.y + 0.2 + 0.6),
            v3FromV2(rightSet[2], pos.y + 0.2 + 0.6),
            v3FromV2(rightSet[3], pos.y + 0.2 + 0.6),
            0.8
        ));
        
        // Head
        provider.getOrThrow(BuiltInParts.HEAD).setFlexBox(new FlexBox(
            v3FromV2(leftSet[2], pos.y + 0.2 + 0.6 + 0.8),
            v3FromV2(leftSet[1], pos.y + 0.2 + 0.6 + 0.8),
            v3FromV2(rightSet[1], pos.y + 0.2 + 0.6 + 0.8),
            v3FromV2(rightSet[2], pos.y + 0.2 + 0.6 + 0.8),
            0.4
        ));
    }
    
    public static void debuffPlayer(ServerPlayerEntity playerEntity) {
        if (!playerEntity.isAlive()) {
            return;
        }
        BodyPartProvider provider = (BodyPartProvider)playerEntity;
        
        // Check for broken legs/feet
        // Each broken part adds a slowness level
        Pair<LegBodyPart, LegBodyPart> legs = BuiltInParts.getLegs(provider);
        Pair<FootBodyPart, FootBodyPart> feet = BuiltInParts.getFeet(provider);
        int slowAmp = -1;
        
        if (legs.getLeft().getHealth() <= 0) slowAmp++;
        if (legs.getRight().getHealth() <= 0) slowAmp++;
        if (feet.getLeft().getHealth() <= 0) slowAmp++;
        if (feet.getRight().getHealth() <= 0) slowAmp++;
        
        if (slowAmp > -1) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, slowAmp, true, true));
        }
        
        // Check for broken arms
        // Each broken one adds a mining fatigue level
        Pair<ArmBodyPart, ArmBodyPart> arms = BuiltInParts.getArms(provider);
        int fatigueAmp = -1;
        if (arms.getLeft().getHealth() <= 0) fatigueAmp++;
        if (arms.getRight().getHealth() <= 0) fatigueAmp++;
        if (fatigueAmp > -1) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 2, fatigueAmp, true, true));
        }
        
        // Blindness and nausea if low on health
        HeadBodyPart head = (HeadBodyPart)provider.getOrThrow(BuiltInParts.HEAD);
        if (head.getHealth() <= 2) {
            // Causes rapid strobing at ~23->~10, be careful!
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 25, 0, true, true));
            // Has no effect at 61 or below
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 62, 0, true, true));
        }
        
        for (AbstractBodyPart limb : provider.getParts()) {
            if (limb.isCritical() && limb.getHealth() <= 0) {
                // Slightly hacky way to make em die with proper death message
                playerEntity.setHealth(-1f);
            }
        }
    }
    
    public static <T extends AbstractBodyPart> float dealDamageToPair(Pair<T, T> pair, float amount) {
        float halved = amount / 2;
        // Deal half to left
        // Deal left-over to right
        // return leftover
        return pair.getRight().takeDamage(
            halved +
                pair.getLeft().takeDamage(halved)
        );
    }
    
    private static Vec3d v3FromV2(Vec2f vec, double height) {
        return new Vec3d(vec.x, height, vec.y);
    }
}

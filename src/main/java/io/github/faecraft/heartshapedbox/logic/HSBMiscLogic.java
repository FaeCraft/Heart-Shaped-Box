package io.github.faecraft.heartshapedbox.logic;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
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

public class HSBMiscLogic {
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
            playerEntity.bodyYaw
        );
    
        Pair<Vec2f[], Vec2f[]> results = playerBoxSlice.splitFromLine(facingLine);
        Vec2f[] leftSet = results.getLeft();
        Vec2f[] rightSet = results.getRight();
        
        // Feet
        provider.getFeet().getLeft().setFlexBox(new FlexBox(
            v3FromV2(leftSet[0], pos.y),
            v3FromV2(leftSet[1], pos.y),
            v3FromV2(leftSet[2], pos.y),
            v3FromV2(leftSet[3], pos.y),
            0.2
        ));
        provider.getFeet().getRight().setFlexBox(new FlexBox(
            v3FromV2(rightSet[0], pos.y),
            v3FromV2(rightSet[1], pos.y),
            v3FromV2(rightSet[2], pos.y),
            v3FromV2(rightSet[3], pos.y),
            0.2
        ));
    
        // Legs
        provider.getLegs().getLeft().setFlexBox(new FlexBox(
            v3FromV2(leftSet[0], pos.y + 0.2),
            v3FromV2(leftSet[1], pos.y + 0.2),
            v3FromV2(leftSet[2], pos.y + 0.2),
            v3FromV2(leftSet[3], pos.y + 0.2),
            0.6
        ));
        provider.getLegs().getRight().setFlexBox(new FlexBox(
            v3FromV2(rightSet[0], pos.y + 0.2),
            v3FromV2(rightSet[1], pos.y + 0.2),
            v3FromV2(rightSet[2], pos.y + 0.2),
            v3FromV2(rightSet[3], pos.y + 0.2),
            0.6
        ));
    
        // Arms
        provider.getArms().getLeft().setFlexBox(new FlexBox(
            v3FromV2(leftSet[0], pos.y + 0.2 + 0.6),
            v3FromV2(leftSet[1], pos.y + 0.2 + 0.6),
            v3FromV2(leftSet[2], pos.y + 0.2 + 0.6),
            v3FromV2(leftSet[3], pos.y + 0.2 + 0.6),
            0.8
        ));
        provider.getArms().getRight().setFlexBox(new FlexBox(
            v3FromV2(rightSet[0], pos.y + 0.2 + 0.6),
            v3FromV2(rightSet[1], pos.y + 0.2 + 0.6),
            v3FromV2(rightSet[2], pos.y + 0.2 + 0.6),
            v3FromV2(rightSet[3], pos.y + 0.2 + 0.6),
            0.8
        ));
        
        // Head
        provider.getHead().setFlexBox(new FlexBox(
            v3FromV2(leftSet[2], pos.y + 0.2 + 0.6 + 0.8),
            v3FromV2(leftSet[1], pos.y + 0.2 + 0.6 + 0.8),
            v3FromV2(rightSet[1], pos.y + 0.2 + 0.6 + 0.8),
            v3FromV2(rightSet[2], pos.y + 0.2 + 0.6 + 0.8),
            0.4
        ));
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
        if (feet.getRight().getHealth() <= 0) slowAmp++;
        
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

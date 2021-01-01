package io.github.faecraft.heartshapedbox.mixin;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.HeadBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;

@Mixin(PlayerEntity.class)
public abstract class BodyPartDuck implements BodyPartProvider {
    public final HeadBodyPart head = new HeadBodyPart();
    
    public final ArmBodyPart leftArm = new ArmBodyPart(BodyPartSide.LEFT);
    public final ArmBodyPart rightArm = new ArmBodyPart(BodyPartSide.RIGHT);
    
    public final LegBodyPart leftLeg = new LegBodyPart(BodyPartSide.LEFT);
    public final LegBodyPart rightLeg = new LegBodyPart(BodyPartSide.RIGHT);
    
    public final FootBodyPart leftFoot = new FootBodyPart(BodyPartSide.LEFT);
    public final FootBodyPart rightFoot = new FootBodyPart(BodyPartSide.RIGHT);
    
    @Override
    public HeadBodyPart getHead() {
        return head;
    }
    
    @Override
    public Pair<ArmBodyPart, ArmBodyPart> getArms() {
        return new Pair<>(leftArm, rightArm);
    }
    
    @Override
    public Pair<LegBodyPart, LegBodyPart> getLegs() {
        return new Pair<>(leftLeg, rightLeg);
    }
    
    @Override
    public Pair<FootBodyPart, FootBodyPart> getFeet() {
        return new Pair<>(leftFoot, rightFoot);
    }
    
    @Override
    public ArrayList<AbstractBodyPart> getAll() {
        ArrayList<AbstractBodyPart> out = new ArrayList<>();
        out.add(head);
        out.add(leftArm);
        out.add(rightArm);
        out.add(leftLeg);
        out.add(rightLeg);
        out.add(leftFoot);
        out.add(rightFoot);
        return out;
    }
}

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
    public HeadBodyPart head = new HeadBodyPart();
    
    public ArmBodyPart leftArm = new ArmBodyPart(BodyPartSide.LEFT);
    public ArmBodyPart rightArm = new ArmBodyPart(BodyPartSide.RIGHT);
    
    public LegBodyPart leftLeg = new LegBodyPart(BodyPartSide.LEFT);
    public LegBodyPart rightLeg = new LegBodyPart(BodyPartSide.RIGHT);
    
    public FootBodyPart leftFoot = new FootBodyPart(BodyPartSide.LEFT);
    public FootBodyPart rightFoot = new FootBodyPart(BodyPartSide.RIGHT);
    
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
    
    @Override
    public ArrayList<AbstractBodyPart> stateCopy() {
        ArrayList<AbstractBodyPart> out = new ArrayList<>();
        out.add(head.copyInto(new HeadBodyPart()));
        out.add(leftArm.copyInto(new ArmBodyPart(BodyPartSide.LEFT)));
        out.add(rightArm.copyInto(new ArmBodyPart(BodyPartSide.RIGHT)));
        out.add(leftLeg.copyInto(new LegBodyPart(BodyPartSide.LEFT)));
        out.add(rightLeg.copyInto(new LegBodyPart(BodyPartSide.RIGHT)));
        out.add(leftFoot.copyInto(new FootBodyPart(BodyPartSide.LEFT)));
        out.add(rightFoot.copyInto(new FootBodyPart(BodyPartSide.RIGHT)));
        return out;
    }
    
    @Override
    public void setFrom(ArrayList<AbstractBodyPart> state) {
        // TODO: Refactor this! It assumes fixed input order
        head = (HeadBodyPart)state.get(0);
        leftArm = (ArmBodyPart)state.get(1);
        rightArm = (ArmBodyPart)state.get(2);
        leftLeg = (LegBodyPart)state.get(3);
        rightLeg = (LegBodyPart)state.get(4);
        leftFoot = (FootBodyPart)state.get(5);
        rightFoot = (FootBodyPart)state.get(6);
    }
}

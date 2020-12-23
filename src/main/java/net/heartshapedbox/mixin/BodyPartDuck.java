package net.heartshapedbox.mixin;

import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.body.BodyPartSide;
import net.heartshapedbox.body.impl.FootBodyPart;
import net.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class BodyPartDuck implements BodyPartProvider {
    public final LegBodyPart leftLeg = new LegBodyPart(BodyPartSide.LEFT);
    public final LegBodyPart rightLeg = new LegBodyPart(BodyPartSide.RIGHT);
    
    public final FootBodyPart leftFoot = new FootBodyPart(BodyPartSide.LEFT);
    public final FootBodyPart rightFoot = new FootBodyPart(BodyPartSide.RIGHT);
    
    @Override
    public Pair<LegBodyPart, LegBodyPart> getLegs() {
        return new Pair<>(leftLeg, rightLeg);
    }
    
    @Override
    public Pair<FootBodyPart, FootBodyPart> getFeet() {
        return new Pair<>(leftFoot, rightFoot);
    }
}

package io.github.faecraft.heartshapedbox.body.impl;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import io.github.faecraft.heartshapedbox.body.BodyPartType;

public class ArmBodyPart extends AbstractBodyPart {
    private final BodyPartSide side;
    
    public ArmBodyPart(BodyPartSide side) {
        this.side = side;
    }
    
    @Override
    public BodyPartType getType() {
        return BodyPartType.ARMS;
    }
    
    @Override
    public BodyPartSide getSide() {
        return side;
    }
    
    @Override
    public float getMaxHealth() {
        return 3;
    }
}

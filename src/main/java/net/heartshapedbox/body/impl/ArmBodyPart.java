package net.heartshapedbox.body.impl;

import net.heartshapedbox.body.AbstractBodyPart;
import net.heartshapedbox.body.BodyPartSide;
import net.heartshapedbox.body.BodyPartType;

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

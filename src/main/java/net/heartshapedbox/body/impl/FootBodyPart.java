package net.heartshapedbox.body.impl;

import net.heartshapedbox.body.AbstractBodyPart;
import net.heartshapedbox.body.BodyPartSide;
import net.heartshapedbox.body.BodyPartType;

public class FootBodyPart extends AbstractBodyPart {
    private final BodyPartSide side;
    
    public FootBodyPart(BodyPartSide side) {
        this.side = side;
    }
    
    @Override
    public BodyPartType getType() {
        return BodyPartType.FEET;
    }
    
    @Override
    public BodyPartSide getSide() {
        return side;
    }
    
    @Override
    public float getMaxHealth() {
        return 2;
    }
}

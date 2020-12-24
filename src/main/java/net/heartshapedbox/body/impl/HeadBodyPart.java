package net.heartshapedbox.body.impl;

import net.heartshapedbox.body.AbstractBodyPart;
import net.heartshapedbox.body.BodyPartSide;
import net.heartshapedbox.body.BodyPartType;

public class HeadBodyPart extends AbstractBodyPart {
    private final BodyPartSide side = BodyPartSide.CENTER;
    
    @Override
    public BodyPartType getType() {
        return BodyPartType.HEAD;
    }
    
    @Override
    public BodyPartSide getSide() {
        return side;
    }
    
    @Override
    public float getMaxHealth() {
        return 6;
    }
}


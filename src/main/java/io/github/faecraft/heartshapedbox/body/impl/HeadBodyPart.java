package io.github.faecraft.heartshapedbox.body.impl;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import io.github.faecraft.heartshapedbox.body.BodyPartType;

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


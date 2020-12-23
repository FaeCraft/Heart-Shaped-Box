package net.heartshapedbox.body.impl;

import net.heartshapedbox.body.AbstractBodyPart;
import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.body.BodyPartSide;
import net.heartshapedbox.body.BodyPartType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

public class LegBodyPart extends AbstractBodyPart {
    private final BodyPartSide side;
    
    public LegBodyPart(BodyPartSide side) {
        this.side = side;
    }
    
    @Override
    public BodyPartType getType() {
        return BodyPartType.LEGS;
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

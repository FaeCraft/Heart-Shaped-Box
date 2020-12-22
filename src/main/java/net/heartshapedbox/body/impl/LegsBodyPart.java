package net.heartshapedbox.body.impl;

import net.heartshapedbox.body.AbstractBodyPart;
import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.body.BodyPartType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

public class LegsBodyPart extends AbstractBodyPart {
    @Override
    public BodyPartType getType() {
        return BodyPartType.LEGS;
    }
    
    @Override
    public float getMaxHealth() {
        return 5.0f;
    }
    
    public static void tickPlayer(ServerPlayerEntity playerEntity) {
        System.out.println(((BodyPartProvider)playerEntity).getLegs().getHealth());
        if (((BodyPartProvider)playerEntity).getLegs().getHealth() <= 0) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 2, true, true));
        }
    }
}

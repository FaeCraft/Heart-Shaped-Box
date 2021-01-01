package net.heartshapedbox.logic.damage.impl;

import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.logic.HSBMiscLogic;
import net.heartshapedbox.logic.damage.DamageHandler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

public class FallDamageHandler implements DamageHandler {
    @Override
    public boolean shouldHandle(DamageSource source) {
        return source.name.equals("fall");
    }
    
    @Override
    public Pair<Boolean, Float> handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        return new Pair<>(false, HSBMiscLogic.dealDamageToPair(
            provider.getLegs(),
            HSBMiscLogic.dealDamageToPair(provider.getFeet(), amount)
        ));
    }
}

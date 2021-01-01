package net.heartshapedbox.logic.damage;

import net.heartshapedbox.body.BodyPartProvider;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

public interface DamageHandler {
    boolean shouldHandle(DamageSource source);
    
    Pair<Boolean, Float> handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount);
}

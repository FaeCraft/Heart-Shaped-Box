package net.heartshapedbox.logic.damage;

import net.heartshapedbox.body.BodyPartProvider;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public interface DamageHandler {
    void handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount);
}

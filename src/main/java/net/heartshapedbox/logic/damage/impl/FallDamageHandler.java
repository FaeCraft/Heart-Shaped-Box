package net.heartshapedbox.logic.damage.impl;

import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.logic.HSBMiscLogic;
import net.heartshapedbox.logic.damage.DamageHandler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class FallDamageHandler implements DamageHandler {
    @Override
    public void handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        player.damage(
            source,
            amount - HSBMiscLogic.dealDamageToPair(
                provider.getLegs(),
                HSBMiscLogic.dealDamageToPair(provider.getFeet(), amount)
            )
        );
    }
}

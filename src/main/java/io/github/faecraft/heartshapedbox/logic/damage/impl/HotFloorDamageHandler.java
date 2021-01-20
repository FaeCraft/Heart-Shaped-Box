package io.github.faecraft.heartshapedbox.logic.damage.impl;

import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.body.BuiltInParts;
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic;
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.Collections;

public class HotFloorDamageHandler implements DamageHandler {
    @Override
    public boolean shouldHandle(DamageSource source) {
        return source.name.equals("hotFloor");
    }
    
    @Override
    public Iterable<ItemStack> getPossibleArmorPieces(ServerPlayerEntity player) {
        // Boots
        return Collections.singleton(player.inventory.armor.get(0));
    }
    
    @Override
    public Pair<Boolean, Float> handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        return new Pair<>(false, HSBMiscLogic.dealDamageToPair(BuiltInParts.getFeet(provider), amount));
    }
}

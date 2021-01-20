package io.github.faecraft.heartshapedbox.logic.damage.impl;

import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.body.BuiltInParts;
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.Collections;

public class FallingBlockDamageHandler implements DamageHandler {
    @Override
    public boolean shouldHandle(DamageSource source) {
        return source.name.equals("anvil") || source.name.equals("fallingBlock");
    }
    
    @Override
    public Iterable<ItemStack> getPossibleArmorPieces(ServerPlayerEntity player) {
        // Helmet
        return Collections.singleton(player.inventory.armor.get(3));
    }
    
    @Override
    public Pair<Boolean, Float> handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        return new Pair<>(false, provider.getOrThrow(BuiltInParts.HEAD).takeDamage(amount));
    }
}

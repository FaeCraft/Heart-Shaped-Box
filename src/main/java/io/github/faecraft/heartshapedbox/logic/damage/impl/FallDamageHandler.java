package io.github.faecraft.heartshapedbox.logic.damage.impl;

import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.body.BuiltInParts;
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic;
import io.github.faecraft.heartshapedbox.logic.damage.DamageHandler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class FallDamageHandler implements DamageHandler {
    @Override
    public boolean shouldHandle(DamageSource source) {
        return source.name.equals("fall");
    }
    
    @Override
    public Iterable<ItemStack> getPossibleArmorPieces(ServerPlayerEntity player) {
        ArrayList<ItemStack> out = new ArrayList<>();
        // Boots
        out.add(player.inventory.armor.get(0));
        // Leggings
        out.add(player.inventory.armor.get(1));
        return out;
    }
    
    @Override
    public Pair<Boolean, Float> handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount) {
        return new Pair<>(false, HSBMiscLogic.dealDamageToPair(
            BuiltInParts.getLegs(provider),
            HSBMiscLogic.dealDamageToPair(BuiltInParts.getFeet(provider), amount)
        ));
    }
}

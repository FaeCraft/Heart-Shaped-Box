package io.github.faecraft.heartshapedbox.logic.damage;

import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

public interface DamageHandler {
    boolean shouldHandle(DamageSource source);
    
    default Iterable<ItemStack> getPossibleArmorPieces(ServerPlayerEntity player) {
        return player.getArmorItems();
    }
    
    Pair<Boolean, Float> handleDamage(ServerPlayerEntity player, BodyPartProvider provider, DamageSource source, float amount);
}

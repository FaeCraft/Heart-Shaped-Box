package io.github.faecraft.heartshapedbox.logic.damage;

import io.github.faecraft.heartshapedbox.bad.BadMixinAtomicFlag;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.logic.damage.impl.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.ArrayList;

public class DamageHandlerDispatcher {
    private static final ArrayList<DamageHandler> handlers = new ArrayList<>();
    
    public static boolean handleDamage(ServerPlayerEntity player, DamageSource source, float amount) {
        BodyPartProvider provider = (BodyPartProvider)player;
        for (DamageHandler possibleHandler : handlers) {
            if (possibleHandler.shouldHandle(source)) {
                // Save a stateCopy of the provider if we want to revert
                CompoundTag stateBefore = provider.toTag();
                
                Pair<Boolean, Float> result = possibleHandler.handleDamage(player, (BodyPartProvider)player, source, amount);
                
                // see BadMixinAtomicFlag for the reason behind this
                BadMixinAtomicFlag.callSuperDamage.set(true);
                boolean didDealDamage = player.damage(source, amount - result.getRight());
                BadMixinAtomicFlag.callSuperDamage.set(false);
                
                // Revert the state if vanilla doesn't like our damage for whatever reason
                if (!didDealDamage) {
                    provider.fromTag(stateBefore);
                }
                
                if (result.getLeft() || amount - result.getRight() <= 0) {
                    return didDealDamage;
                }
            }
        }
        return false;
    }
    
    public static void registerHandlers() {
        // Lower indices get tested first (i.e higher priority)
        handlers.add(new ProjectileDamageHandler());
        handlers.add(new FallingBlockDamageHandler());
        handlers.add(new FallDamageHandler());
        handlers.add(new HotFloorDamageHandler());
        handlers.add(new GenericDamageHandler());
    }
}

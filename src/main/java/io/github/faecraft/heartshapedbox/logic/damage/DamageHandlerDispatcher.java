package io.github.faecraft.heartshapedbox.logic.damage;

import io.github.faecraft.heartshapedbox.bad.BadMixinAtomicFlags;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.logic.damage.impl.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Pair;

import java.util.ArrayList;

public class DamageHandlerDispatcher {
    private static final ArrayList<DamageHandler> handlers = new ArrayList<>();

    public static boolean handleDamage(ServerPlayerEntity player, DamageSource source, float amount) {
        BodyPartProvider provider = (BodyPartProvider)player;
    
        if (player.hasStatusEffect(StatusEffects.RESISTANCE) && source != DamageSource.OUT_OF_WORLD) {
            //noinspection ConstantConditions
            int k = (player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
            int j = 25 - k;
            float f = amount * (float)j;
            float g = amount;
            amount = Math.max(f / 25.0F, 0.0F);
            float h = g - amount;
            if (h > 0.0F && h < 3.4028235E37F) {
                player.increaseStat(Stats.DAMAGE_RESISTED, Math.round(h * 10.0F));
            }
        }
    
        // Done to deal damage in a single go
        float collectedDamage = 0f;
        // Saves state before damage handled
        CompoundTag stateBefore = provider.writeToTag();
        for (DamageHandler possibleHandler : handlers) {
            if (possibleHandler.shouldHandle(source)) {
                Pair<Boolean, Float> result = possibleHandler.handleDamage(player, (BodyPartProvider)player, source, amount);

                collectedDamage += amount - result.getRight();
                amount = result.getRight();

                if (result.getLeft() || result.getRight() <= 0) {
                    break;
                }
            }
        }
        // Try to deal damage, roll back if failed
        // see BadMixinAtomicFlags for the reason behind this
        BadMixinAtomicFlags.callSuperDamage.set(true);
        boolean didDealDamage = player.damage(source, collectedDamage);
        BadMixinAtomicFlags.callSuperDamage.set(false);

        // Revert the state if vanilla doesn't like our damage for whatever reason
        if (!didDealDamage) {
            provider.readFromTag(stateBefore);
        }
        return didDealDamage;
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

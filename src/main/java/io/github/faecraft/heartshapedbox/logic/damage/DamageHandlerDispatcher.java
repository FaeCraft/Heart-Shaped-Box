package io.github.faecraft.heartshapedbox.logic.damage;

import io.github.faecraft.heartshapedbox.logic.damage.impl.*;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.ArrayList;

public class DamageHandlerDispatcher {
    private static final ArrayList<DamageHandler> handlers = new ArrayList<>();
    
    public static void handleDamage(ServerPlayerEntity player, DamageSource source, float amount) {
        for (DamageHandler possibleHandler : handlers) {
            if (possibleHandler.shouldHandle(source)) {
                Pair<Boolean, Float> result = possibleHandler.handleDamage(player, (BodyPartProvider)player, source, amount);
                player.damage(source,amount - result.getRight());
                if (result.getLeft() || amount - result.getRight() <= 0) {
                    break;
                }
            }
        }
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

package net.heartshapedbox.logic.damage;

import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.logic.damage.impl.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public class DamageHandlerDispatcher {
    private static final ArrayList<DamageHandler> handlers = new ArrayList<>();
    
    public static void handleDamage(ServerPlayerEntity player, DamageSource source, float amount) {
        for (DamageHandler possibleHandler : handlers) {
            if (possibleHandler.shouldHandle(source)) {
                player.damage(source,amount - possibleHandler.handleDamage(player, (BodyPartProvider)player, source, amount));
                break;
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

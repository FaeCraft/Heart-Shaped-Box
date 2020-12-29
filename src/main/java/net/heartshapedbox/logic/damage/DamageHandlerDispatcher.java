package net.heartshapedbox.logic.damage;

import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.logic.damage.impl.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.function.Predicate;

public class DamageHandlerDispatcher {
    private static final ArrayList<Pair<Predicate<DamageSource>, DamageHandler>> handlers = new ArrayList<>();
    
    public static void handleDamage(ServerPlayerEntity player, DamageSource source, float amount) {
        for (Pair<Predicate<DamageSource>, DamageHandler> possibleHandler : handlers) {
            if (possibleHandler.getLeft().test(source)) {
                possibleHandler.getRight().handleDamage(player, (BodyPartProvider)player, source, amount);
                break;
            }
        }
    }
    
    public static void registerHandlers() {
        handlers.add(new Pair<>(
            source -> source instanceof ProjectileDamageSource && source.getAttacker() != null,
            new ProjectileDamageHandler()
        ));
        handlers.add(new Pair<>(
            source -> source.name.equals("anvil") || source.name.equals("fallingBlock"),
            new FallingBlockDamageHandler()
        ));
        handlers.add(new Pair<>(
            source -> source.name.equals("fall"),
            new FallDamageHandler()
        ));
        handlers.add(new Pair<>(
            source -> source.name.equals("hotFloor"),
            new HotFloorDamageHandler()
        ));
        handlers.add(new Pair<>(
            source -> true,
            new GenericDamageHandler()
        ));
    }
}

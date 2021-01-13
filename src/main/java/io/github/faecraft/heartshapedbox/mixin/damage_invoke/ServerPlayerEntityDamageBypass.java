package io.github.faecraft.heartshapedbox.mixin.damage_invoke;

import com.mojang.authlib.GameProfile;
import io.github.faecraft.heartshapedbox.bad.BadMixinAtomicFlags;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
abstract public class ServerPlayerEntityDamageBypass extends PlayerEntity {
    public ServerPlayerEntityDamageBypass(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }
    
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void sendToPlayerEntitySuperDamageCall(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (BadMixinAtomicFlags.callSuperDamage.get()) {
            cir.setReturnValue(super.damage(source, amount));
        }
    }
}

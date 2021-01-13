package io.github.faecraft.heartshapedbox.mixin;

import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        S2CBodyPartSyncPacket packet = S2CBodyPartSyncPacket.from(player);

        packet.send(player);
    }
}

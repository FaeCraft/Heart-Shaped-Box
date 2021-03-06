package io.github.faecraft.heartshapedbox.mixin;

import io.github.faecraft.heartshapedbox.mixin.accessor.CustomPayloadChannelAccessor;
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class SyncDataOnJoinMixin {
    @Inject(method = "onCustomPayload", at = @At(value = "HEAD"))
    public void syncHSBLimbDataOnJoin(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        // BRAND packet is sent after the ClientPlayerEntity is created (see ClientPlayNetworkHandler.onGameJoin)
        // So when this is sent its safe to assume the client player exists
        // At no risk to the server, because only the client would crash if the ClientPlayerEntity didn't exist
        if (((CustomPayloadChannelAccessor)packet).getChannel() == CustomPayloadC2SPacket.BRAND) {
            ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler)(Object)this;
            
            S2CBodyPartSyncPacket.from(handler.player).send(handler.player);
        }
    }
}

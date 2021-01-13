package io.github.faecraft.heartshapedbox.main;

import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class HSBClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        setupNetworking();
    }

    public void setupNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(
                S2CBodyPartSyncPacket.IDENTIFIER,
                (client, handler, buf, responseSender) -> {
                    S2CBodyPartSyncPacket.update(buf, (BodyPartProvider) client.player);
                }
        );
    }
}

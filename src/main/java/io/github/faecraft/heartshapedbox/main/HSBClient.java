package io.github.faecraft.heartshapedbox.main;

import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.networking.S2CSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class HSBClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        setupNetworking();
    }

    public void setupNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(
                S2CSyncPacket.IDENTIFIER,
                (client, handler, buf, responseSender) -> {
                    S2CSyncPacket.update(buf, (BodyPartProvider) client.player);
                }
        );
    }
}

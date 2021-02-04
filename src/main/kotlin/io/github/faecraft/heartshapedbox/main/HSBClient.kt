package io.github.faecraft.heartshapedbox.main

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

class HSBClient : ClientModInitializer {
    override fun onInitializeClient() {
        setupNetworking()
    }

    private fun setupNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(S2CBodyPartSyncPacket.IDENTIFIER)
        { client: MinecraftClient, _: ClientPlayNetworkHandler, buf: PacketByteBuf, _: PacketSender ->
            S2CBodyPartSyncPacket.update(buf, client.player as BodyPartProvider)
        }
    }
}

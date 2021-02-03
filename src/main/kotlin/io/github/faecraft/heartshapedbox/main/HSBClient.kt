package io.github.faecraft.heartshapedbox.main

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import net.fabricmc.fabric.api.networking.v1.PacketSender
import io.github.faecraft.heartshapedbox.body.BodyPartProvider

class HSBClient : ClientModInitializer {
    override fun onInitializeClient() {
        setupNetworking()
    }

    fun setupNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(
            S2CBodyPartSyncPacket.IDENTIFIER
        ) { client: MinecraftClient, handler: ClientPlayNetworkHandler, buf: PacketByteBuf, responseSender: PacketSender ->
            S2CBodyPartSyncPacket.update(
                buf,
                client.player as BodyPartProvider
            )
        }
    }
}

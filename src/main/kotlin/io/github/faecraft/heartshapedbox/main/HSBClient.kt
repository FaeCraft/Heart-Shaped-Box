package io.github.faecraft.heartshapedbox.main

import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.client.HealthDisplayScreen
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.options.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.network.PacketByteBuf
import org.lwjgl.glfw.GLFW

public class HSBClient : ClientModInitializer {
    override fun onInitializeClient() {
        setupNetworking()

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            while (openHealthScreenKeybind.wasPressed()) {
                openHealthScreen()
            }
        })
    }

    private fun setupNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(S2CBodyPartSyncPacket.IDENTIFIER)
        { client: MinecraftClient, _: ClientPlayNetworkHandler, buf: PacketByteBuf, _: PacketSender ->
            S2CBodyPartSyncPacket.update(buf, client.player as BodyPartProvider)
        }
    }

    public fun openHealthScreen() {
        val instance = MinecraftClient.getInstance()
        // Make sure another screen isn't open
        // So people cant accidentally bind to left mouse
        // And constantly open the screen
        if (instance.currentScreen == null) {
            instance.openScreen(HealthDisplayScreen(false))
        }
    }

    public companion object {
        public val openHealthScreenKeybind: KeyBinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.${HSBMain.MOD_ID}.open_health_screen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.${HSBMain.MOD_ID}.keybinds"
            )
        )
    }
}

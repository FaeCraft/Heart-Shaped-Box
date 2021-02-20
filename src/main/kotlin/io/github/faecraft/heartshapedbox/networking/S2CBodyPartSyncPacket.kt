package io.github.faecraft.heartshapedbox.networking

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart
import io.github.faecraft.heartshapedbox.body.BodyPartProvider
import io.github.faecraft.heartshapedbox.main.HSBMain
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import java.util.*

public class S2CBodyPartSyncPacket() {
    private val parts = HashSet<AbstractBodyPart>()

    public constructor(provider: BodyPartProvider) : this() {
        parts.addAll(provider.parts)
    }

    public fun addPart(part: AbstractBodyPart) {
        parts.add(part)
    }

    private fun write(buffer: PacketByteBuf) {
        for (part in parts) {
            buffer.writeIdentifier(part.getIdentifier())
            buffer.writeFloat(part.getHealth())
            buffer.writeFloat(part.getMaxHealth())
        }
    }

    public fun send(player: ServerPlayerEntity) {
        // TODO: Remove logging call
        LOGGER.info("Sending packet to player: " + player.displayName.asString())
        val buffer = PacketByteBufs.create()
        write(buffer)
        ServerPlayNetworking.send(player, IDENTIFIER, buffer)
    }

    public companion object {
        private val LOGGER = LogManager.getLogger("S2CBodyPartSyncPacket")
        public val IDENTIFIER: Identifier = Identifier(HSBMain.MOD_ID, "body_part_sync")

        public fun update(buffer: PacketByteBuf, provider: BodyPartProvider) {
            while (buffer.isReadable) {
                val id = buffer.readIdentifier()
                val health = buffer.readFloat()
                val maxHealth = buffer.readFloat()
                // TODO: Remove logging calls
                LOGGER.info("UPDATE | Provider: $provider")
                LOGGER.info("UPDATE | ID: $id Health: $health Max Health: $maxHealth")
                val optionalPart = provider.maybeGet(id)
                if (optionalPart.isPresent) {
                    val part = optionalPart.get()
                    part.setHealth(health)
                    part.setMaxHealth(maxHealth)
                }
            }
        }

        @JvmStatic
        public fun from(player: ServerPlayerEntity): S2CBodyPartSyncPacket =
            S2CBodyPartSyncPacket(player as BodyPartProvider)
    }
}

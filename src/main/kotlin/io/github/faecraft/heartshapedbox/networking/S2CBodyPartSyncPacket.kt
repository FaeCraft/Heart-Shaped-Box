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

class S2CBodyPartSyncPacket() {
    private val parts = HashSet<AbstractBodyPart>()

    constructor(provider: BodyPartProvider) : this() {
        parts.addAll(provider.parts)
    }

    fun addPart(part: AbstractBodyPart) {
        parts.add(part)
    }

    private fun write(buffer: PacketByteBuf) {
        for (part in parts) {
            buffer.writeIdentifier(part.getIdentifier())
            buffer.writeFloat(part.getHealth())
            buffer.writeFloat(part.getMaxHealth())
        }
    }

    fun send(player: ServerPlayerEntity) {
        LOGGER.info("Sending packet to player: " + player.displayName.asString())
        val buffer = PacketByteBufs.create()
        write(buffer)
        ServerPlayNetworking.send(player, IDENTIFIER, buffer)
    }

    companion object {
        private val LOGGER = LogManager.getLogger("S2CBodyPartSyncPacket")
        val IDENTIFIER = Identifier(HSBMain.MOD_ID, "body_part_sync")
        fun update(buffer: PacketByteBuf, provider: BodyPartProvider) {
            while (buffer.isReadable) {
                val id = buffer.readIdentifier()
                val health = buffer.readFloat()
                val maxHealth = buffer.readFloat()
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

        fun from(player: ServerPlayerEntity): S2CBodyPartSyncPacket {
            return S2CBodyPartSyncPacket(player as BodyPartProvider)
        }
    }
}

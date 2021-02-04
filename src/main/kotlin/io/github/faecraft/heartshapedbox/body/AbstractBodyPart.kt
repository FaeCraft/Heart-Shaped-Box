package io.github.faecraft.heartshapedbox.body

import io.github.faecraft.heartshapedbox.math.FlexBox
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket
import io.github.faecraft.heartshapedbox.util.SettableLazy
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

abstract class AbstractBodyPart(private val owner: PlayerEntity) {
    private var internalMaxHealth by SettableLazy { getDefaultMaxHealth() }
    private var health = internalMaxHealth
    var flexBox = FlexBox.zero()

    open val isCritical: Boolean = false

    abstract fun getIdentifier(): Identifier
    abstract fun getSide(): BodyPartSide
    abstract fun getDefaultMaxHealth(): Float

    fun takeDamage(amount: Float): Float {
        if (amount > health) {
            val used = amount - health
            setHealth(0f)
            return used
        }
        setHealth(health - amount)
        return 0f
    }

    private fun update() {
        if (owner is ServerPlayerEntity) {
            val packet = S2CBodyPartSyncPacket()
            packet.addPart(this)
            packet.send(owner)
        }
    }

    fun toTag(tag: CompoundTag) {
        val data = CompoundTag()
        data.putFloat("health", health)
        data.putFloat("maxHealth", internalMaxHealth)
        tag.put(getIdentifier().toString(), data)
    }

    fun fromTag(tag: CompoundTag) {
        health = tag.getFloat("health")
        internalMaxHealth = tag.getFloat("maxHealth")
    }

    fun getHealth(): Float {
        return health
    }

    fun setHealth(amount: Float) {
        if (amount != health) {
            // TODO: Remove logging call later
            LOGGER.info("Health changed (" + getIdentifier() + ") " + health + " -> " + amount)
            health = amount
            update()
        }
    }

    fun getMaxHealth(): Float {
        return internalMaxHealth
    }

    fun setMaxHealth(amount: Float) {
        // TODO: Remove logging call later
        LOGGER.info("Max health changed (" + getIdentifier() + ") " + health + " -> " + amount)
        if (amount != internalMaxHealth) {
            internalMaxHealth = amount
            update()
        }
    }

    final override fun toString(): String {
        return "BodyPart(" + getIdentifier().toString() + ") {" +
                "maxHealth=" + internalMaxHealth +
                ", health=" + health +
                '}'
    }

    companion object {
        private val LOGGER = LogManager.getLogger("AbstractBodyPart")
    }
}

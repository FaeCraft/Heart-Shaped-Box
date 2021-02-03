/*
 Apparently calling "getDefaultMaxHealth" inside the constructor can have issues in multithreading environments
 but I don't care enough right now to restructure this
 so it stays
 - P03W
*/
@file:Suppress("LeakingThis")

package io.github.faecraft.heartshapedbox.body

import io.github.faecraft.heartshapedbox.math.FlexBox
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

abstract class AbstractBodyPart(private val owner: PlayerEntity) {
    private var maxHealth = getDefaultMaxHealth()
    private var health = maxHealth
    var flexBox = FlexBox.zero()
    private fun update() {
        if (owner is ServerPlayerEntity) {
            val packet = S2CBodyPartSyncPacket()
            packet.addPart(this)
            packet.send(owner)
        }
    }

    open val isCritical: Boolean = false

    fun toTag(tag: CompoundTag) {
        val data = CompoundTag()
        data.putFloat("health", health)
        data.putFloat("maxHealth", maxHealth)
        tag.put(getIdentifier().toString(), data)
    }

    fun fromTag(tag: CompoundTag) {
        health = tag.getFloat("health")
        maxHealth = tag.getFloat("maxHealth")
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
        return maxHealth
    }

    fun setMaxHealth(amount: Float) {
        // TODO: Remove logging call later
        LOGGER.info("Max health changed (" + getIdentifier() + ") " + health + " -> " + amount)
        if (amount != maxHealth) {
            maxHealth = amount
            update()
        }
    }

    fun takeDamage(amount: Float): Float {
        if (amount > health) {
            val used = amount - health
            setHealth(0f)
            return used
        }
        setHealth(health - amount)
        return 0f
    }

    override fun toString(): String {
        return "BodyPart(" + getIdentifier().toString() + ") {" +
                "maxHealth=" + maxHealth +
                ", health=" + health +
                '}'
    }

    companion object {
        private val LOGGER = LogManager.getLogger("AbstractBodyPart")
    }

    abstract fun getIdentifier(): Identifier
    abstract fun getSide(): BodyPartSide
    abstract fun getDefaultMaxHealth(): Float
}

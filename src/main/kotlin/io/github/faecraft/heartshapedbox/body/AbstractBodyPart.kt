package io.github.faecraft.heartshapedbox.body

import io.github.faecraft.heartshapedbox.math.FlexBox
import io.github.faecraft.heartshapedbox.networking.S2CBodyPartSyncPacket
import io.github.faecraft.heartshapedbox.util.QuadSame
import io.github.faecraft.heartshapedbox.util.SettableLazy
import net.minecraft.entity.EntityPose
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f
import org.apache.logging.log4j.LogManager

public abstract class AbstractBodyPart(private val owner: PlayerEntity) {
    private var internalMaxHealth by SettableLazy { getDefaultMaxHealth() }
    private var health = internalMaxHealth
    public var flexBox: FlexBox = FlexBox.ZERO

    public open val isCritical: Boolean = false

    public abstract fun getIdentifier(): Identifier
    public abstract fun getSide(): BodyPartSide
    public abstract fun getDefaultMaxHealth(): Float
    public abstract fun getAffectingArmor(player: ServerPlayerEntity): List<ItemStack>
    public abstract fun generateFlexBox(
        player: ServerPlayerEntity,
        pose: EntityPose,
        leftSet: QuadSame<Vec2f>,
        rightSet: QuadSame<Vec2f>
    ): FlexBox

    /**
     * @return The amount dealt to the body part
     */
    public fun takeDamage(amount: Float): Float {
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

    public fun toTag(tag: CompoundTag) {
        val data = CompoundTag()
        data.putFloat("health", health)
        data.putFloat("maxHealth", internalMaxHealth)
        tag.put(getIdentifier().toString(), data)
    }

    public fun fromTag(tag: CompoundTag) {
        health = tag.getFloat("health")
        internalMaxHealth = tag.getFloat("maxHealth")
    }

    public fun getHealth(): Float = health

    public fun setHealth(amount: Float) {
        if (amount != health) {
            // TODO: Remove logging call later
            LOGGER.info("Health changed (" + getIdentifier() + ") " + health + " -> " + amount)
            health = amount
            update()
        }
    }

    public fun getMaxHealth(): Float = internalMaxHealth

    public fun setMaxHealth(amount: Float) {
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

    public companion object {
        private val LOGGER = LogManager.getLogger("AbstractBodyPart")
    }
}

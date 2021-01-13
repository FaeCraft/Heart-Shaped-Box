package io.github.faecraft.heartshapedbox.body;

import io.github.faecraft.heartshapedbox.math.FlexBox;
import io.github.faecraft.heartshapedbox.networking.S2CSyncPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public abstract class AbstractBodyPart {
    private float maxHealth = getDefaultMaxHealth();
    private float health = maxHealth;
    private FlexBox flexBox = FlexBox.zero();

    private final PlayerEntity owner;

    public AbstractBodyPart(PlayerEntity owner) {
        this.owner = owner;
    }

    public void update() {
        if (owner instanceof ServerPlayerEntity) {
            S2CSyncPacket packet = new S2CSyncPacket();

            packet.addPart(this);
            packet.send((ServerPlayerEntity) owner);
        }
    }

    public abstract @NotNull Identifier getIdentifier();
    
    public abstract BodyPartSide getSide();
    
    public abstract float getDefaultMaxHealth();
    
    public void toTag(CompoundTag tag) {
        CompoundTag data = new CompoundTag();
        data.putFloat("health", health);
        data.putFloat("maxHealth", maxHealth);
        tag.put(getIdentifier().toString(), data);
    }
    
    public void fromTag(CompoundTag tag) {
        health = tag.getFloat("health");
        maxHealth = tag.getFloat("maxHealth");
    }
    
    public float getHealth() {
        return health;
    }
    
    public void setHealth(float amount) {
        if (amount != health) {
            health = amount;

            update();
        }
    }
    
    public float getMaxHealth() {
        return maxHealth;
    }
    
    public void setMaxHealth(float amount) {
        if (amount != maxHealth) {
            maxHealth = amount;

            update();
        }
    }
    
    public float takeDamage(float amount) {
        if (amount > health) {
            float used = amount - health;
            health = 0;
            return used;
        }
        health -= amount;
        return 0;
    }
    
    @Override
    public String toString() {
        return "BodyPart(" + getIdentifier().toString() + ") {" +
            "maxHealth=" + maxHealth +
            ", health=" + health +
            '}';
    }
    
    public FlexBox getFlexBox() {
        return flexBox;
    }
    
    public void setFlexBox(FlexBox flexBox) {
        this.flexBox = flexBox;
    }
}

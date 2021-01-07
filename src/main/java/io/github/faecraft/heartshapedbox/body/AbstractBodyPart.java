package io.github.faecraft.heartshapedbox.body;

import io.github.faecraft.heartshapedbox.math.FlexBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBodyPart {
    private float maxHealth = getDefaultMaxHealth();
    private float health = maxHealth;
    private FlexBox flexBox = FlexBox.zero();
    
    public abstract @NotNull Identifier getIdentifier();
    
    public abstract BodyPartSide getSide();
    
    public abstract float getDefaultMaxHealth();
    
    public void toTag(CompoundTag tag) {
        CompoundTag info = new CompoundTag();
        info.putFloat("health", health);
        info.putFloat("maxHealth", maxHealth);
        tag.put(getIdentifier().toString(), info);
    }
    
    public void fromTag(CompoundTag tag) {
        CompoundTag info = tag.getCompound(getIdentifier().toString());
        health = info.getFloat("health");
        maxHealth = info.getFloat("maxHealth");
    }
    
    public float getHealth() {
        return health;
    }
    
    public void setHealth(float amount) {
        health = amount;
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
            "maxHealth=" + getDefaultMaxHealth() +
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

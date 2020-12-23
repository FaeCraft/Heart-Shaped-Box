package net.heartshapedbox.body;

import net.minecraft.nbt.CompoundTag;

public abstract class AbstractBodyPart {
    private float health = getMaxHealth();
    
    public abstract BodyPartType getType();
    
    public abstract BodyPartSide getSide();
    
    public abstract float getMaxHealth();
    
    public void toTag(CompoundTag tag) {
        CompoundTag info = new CompoundTag();
        info.putFloat("health", health);
        tag.put(getType().name() + " - " + getSide().name(), info);
    }
    
    public void fromTag(CompoundTag tag) {
        CompoundTag info = tag.getCompound(getType().name() + " - " + getSide().name());
        health = info.getFloat("health");
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
        return getType().name() + "{" +
            "maxHealth=" + getMaxHealth() +
            ", health=" + health +
            '}';
    }
}

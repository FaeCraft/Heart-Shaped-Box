package net.heartshapedbox.body;

public abstract class AbstractBodyPart {
    private float health = getMaxHealth();
    
    public abstract BodyPartType getType();
    
    public abstract float getMaxHealth();
    
    public float getHealth() {
        return health;
    }
    
    public float takeDamage(float amount) {
        if (amount > health) {
            amount = health;
        }
        health -= amount;
        return amount;
    }
    
    @Override
    public String toString() {
        return getType().name() + "{" +
            "maxHealth=" + getMaxHealth() +
            ", health=" + health +
            '}';
    }
}

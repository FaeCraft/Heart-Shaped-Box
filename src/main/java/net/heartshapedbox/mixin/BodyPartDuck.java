package net.heartshapedbox.mixin;

import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.body.impl.LegsBodyPart;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class BodyPartDuck implements BodyPartProvider {
    private final LegsBodyPart legs = new LegsBodyPart();
    
    @Override
    public LegsBodyPart getLegs() {
        return legs;
    }
}

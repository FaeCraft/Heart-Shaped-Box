package io.github.faecraft.heartshapedbox.mixin;

import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.logic.HSBMiscLogic;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class AlertChangeHealthMixin {
    @Inject(method = "setHealth", at = @At("HEAD"))
    public void alertHealthChange(float health, CallbackInfo ci) {
        //noinspection ConstantConditions
        if ((Object)this instanceof ServerPlayerEntity) {
            HSBMiscLogic.forceHealthChangeToLimbs(health, (BodyPartProvider)this);
        }
    }
}

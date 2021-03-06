package io.github.faecraft.heartshapedbox.mixin;

import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.main.HSBMain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class SaveDamageMixin {
    @Inject(method = "writeCustomDataToTag", at = @At("RETURN"))
    public void serializeBodyParts(CompoundTag tag, CallbackInfo ci) {
        BodyPartProvider provider = (BodyPartProvider)this;
    
        tag.put(HSBMain.MOD_ID, provider.writeToTag());
    }
    
    @Inject(method = "readCustomDataFromTag", at = @At("RETURN"))
    public void deserializeBodyParts(CompoundTag tag, CallbackInfo ci) {
        BodyPartProvider provider = (BodyPartProvider)this;
        
        CompoundTag partsTag = tag.getCompound(HSBMain.MOD_ID);
    
        provider.readFromTag(partsTag);
    }
}

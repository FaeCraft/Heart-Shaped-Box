package net.heartshapedbox.mixin;

import net.heartshapedbox.body.AbstractBodyPart;
import net.heartshapedbox.body.BodyPartProvider;
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
        
        CompoundTag partsTag = new CompoundTag();
    
        for (AbstractBodyPart limb : provider.getAll()) {
            limb.toTag(partsTag);
        }
        
        tag.put("hsb", partsTag);
    }
    
    @Inject(method = "readCustomDataFromTag", at = @At("RETURN"))
    public void deserializeBodyParts(CompoundTag tag, CallbackInfo ci) {
        BodyPartProvider provider = (BodyPartProvider)this;
        
        CompoundTag partsTag = tag.getCompound("hsb");
    
        for (AbstractBodyPart limb : provider.getAll()) {
            limb.fromTag(partsTag);
        }
    }
}

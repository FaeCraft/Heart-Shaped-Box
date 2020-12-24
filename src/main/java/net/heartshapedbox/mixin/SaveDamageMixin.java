package net.heartshapedbox.mixin;

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
    
        // Head
        provider.getHead().toTag(partsTag);
        
        // Arms
        provider.getArms().getLeft().toTag(partsTag);
        provider.getArms().getRight().toTag(partsTag);
        
        // Legs
        provider.getLegs().getLeft().toTag(partsTag);
        provider.getLegs().getRight().toTag(partsTag);
    
        // Feet
        provider.getFeet().getLeft().toTag(partsTag);
        provider.getFeet().getRight().toTag(partsTag);
        
        tag.put("hsb", partsTag);
    }
    
    @Inject(method = "readCustomDataFromTag", at = @At("RETURN"))
    public void deserializeBodyParts(CompoundTag tag, CallbackInfo ci) {
        BodyPartProvider provider = (BodyPartProvider)this;
        
        CompoundTag partsTag = tag.getCompound("hsb");
    
        // Head
        provider.getHead().fromTag(partsTag);
        
        // Arms
        provider.getArms().getLeft().fromTag(partsTag);
        provider.getArms().getRight().fromTag(partsTag);
        
        // Legs
        provider.getLegs().getLeft().fromTag(partsTag);
        provider.getLegs().getRight().fromTag(partsTag);
    
        // Feet
        provider.getFeet().getLeft().fromTag(partsTag);
        provider.getFeet().getRight().fromTag(partsTag);
    }
}

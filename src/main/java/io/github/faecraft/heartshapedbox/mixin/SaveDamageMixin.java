package io.github.faecraft.heartshapedbox.mixin;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public class SaveDamageMixin {
    @Inject(method = "writeCustomDataToTag", at = @At("RETURN"))
    public void serializeBodyParts(CompoundTag tag, CallbackInfo ci) {
        BodyPartProvider provider = (BodyPartProvider)this;
        
        CompoundTag partsTag = new CompoundTag();
    
        partsTag.put("hsb", provider.toTag());
        
        tag.put("hsb", partsTag);
    }
    
    @Inject(method = "readCustomDataFromTag", at = @At("RETURN"))
    public void deserializeBodyParts(CompoundTag tag, CallbackInfo ci) {
        BodyPartProvider provider = (BodyPartProvider)this;
        
        CompoundTag partsTag = tag.getCompound("hsb");
    
        for (String key : partsTag.getKeys()) {
            Optional<AbstractBodyPart> optional = provider.getFromIdentifier(new Identifier(key));
            optional.ifPresent(abstractBodyPart -> abstractBodyPart.fromTag(partsTag.getCompound(key)));
        }
    }
}

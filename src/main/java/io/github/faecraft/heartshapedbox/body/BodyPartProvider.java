package io.github.faecraft.heartshapedbox.body;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Optional;

// Duck, make sure to add to this for each part
public interface BodyPartProvider {
    ArrayList<AbstractBodyPart> getParts();
    
    CompoundTag toTag();
    
    void fromTag(CompoundTag tag);
    
    Optional<AbstractBodyPart> getFromIdentifier(Identifier identifier);
}

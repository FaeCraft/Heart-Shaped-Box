package io.github.faecraft.heartshapedbox.body;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Duck, applied in BodyPartDuck mixin
 */
public interface BodyPartProvider {
    HashMap<Identifier, AbstractBodyPart> getPartsMap();
    
    ArrayList<AbstractBodyPart> getParts();
    
    CompoundTag writeToTag();
    
    void readFromTag(CompoundTag tag);
    
    Optional<AbstractBodyPart> maybeGet(Identifier identifier);
    
    @Nullable AbstractBodyPart getOrNull(Identifier identifier);
    
    AbstractBodyPart getOrThrow(Identifier identifier);
}

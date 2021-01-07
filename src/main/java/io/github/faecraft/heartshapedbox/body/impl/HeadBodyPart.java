package io.github.faecraft.heartshapedbox.body.impl;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class HeadBodyPart extends AbstractBodyPart {
    private final Identifier identifier = new Identifier("hsb:head");
    
    @Override
    public @NotNull Identifier getIdentifier() {
        return identifier;
    }
    
    @Override
    public BodyPartSide getSide() {
        return BodyPartSide.CENTER;
    }
    
    @Override
    public float getDefaultMaxHealth() {
        return 6;
    }
}


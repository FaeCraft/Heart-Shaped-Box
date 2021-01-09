package io.github.faecraft.heartshapedbox.body.impl;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LegBodyPart extends AbstractBodyPart {
    private final BodyPartSide side;
    private final Identifier identifier;
    
    public LegBodyPart(BodyPartSide side) {
        this.side = side;
        this.identifier = new Identifier("hsb:leg/" + side.lowerName());
    }
    
    @Override
    public @NotNull Identifier getIdentifier() {
        return identifier;
    }
    
    @Override
    public BodyPartSide getSide() {
        return side;
    }
    
    @Override
    public float getDefaultMaxHealth() {
        return 3;
    }
}

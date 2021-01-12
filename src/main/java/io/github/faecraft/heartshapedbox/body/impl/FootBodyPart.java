package io.github.faecraft.heartshapedbox.body.impl;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import io.github.faecraft.heartshapedbox.body.BuiltInParts;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class FootBodyPart extends AbstractBodyPart {
    private final BodyPartSide side;
    private final Identifier identifier;
    
    public FootBodyPart(BodyPartSide side) {
        this.side = side;
        this.identifier = side == BodyPartSide.LEFT ? BuiltInParts.LEFT_FOOT : BuiltInParts.RIGHT_FOOT;
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
        return 2;
    }
}

package io.github.faecraft.heartshapedbox.body.impl;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ArmBodyPart extends AbstractBodyPart {
    private final BodyPartSide side;
    private final Identifier identifier;
    
    public ArmBodyPart(BodyPartSide side) {
        this.side = side;
        this.identifier = new Identifier("hsb:arm/" + side.lowerName());
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

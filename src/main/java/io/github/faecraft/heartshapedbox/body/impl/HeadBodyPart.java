package io.github.faecraft.heartshapedbox.body.impl;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import io.github.faecraft.heartshapedbox.body.BuiltInParts;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class HeadBodyPart extends AbstractBodyPart {
    private final Identifier identifier = BuiltInParts.HEAD;

    public HeadBodyPart(PlayerEntity owner) {
        super(owner);
    }

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
        return 4;
    }
    
    @Override
    public boolean isCritical() {
        return true;
    }
}


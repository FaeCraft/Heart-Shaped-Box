package io.github.faecraft.heartshapedbox.body;

import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import static io.github.faecraft.heartshapedbox.body.BodyPartSide.LEFT;
import static io.github.faecraft.heartshapedbox.body.BodyPartSide.RIGHT;
import static io.github.faecraft.heartshapedbox.main.HSBMain.MOD_ID;

public class BuiltInParts {
    // Head
    public static final Identifier HEAD = new Identifier(MOD_ID, "head");
    
    // Arms
    public static final Identifier LEFT_ARM = new Identifier(MOD_ID, "arm/" + LEFT.lowerName());
    public static final Identifier RIGHT_ARM = new Identifier(MOD_ID, "arm/" + RIGHT.lowerName());
    
    // Legs
    public static final Identifier LEFT_LEG = new Identifier(MOD_ID, "leg/" + LEFT.lowerName());
    public static final Identifier RIGHT_LEG = new Identifier(MOD_ID, "leg/" + RIGHT.lowerName());
    
    // Feet
    public static final Identifier LEFT_FOOT = new Identifier(MOD_ID, "foot/" + LEFT.lowerName());
    public static final Identifier RIGHT_FOOT = new Identifier(MOD_ID, "foot/" + RIGHT.lowerName());
    
    
    // Pairs
    
    public static Pair<ArmBodyPart, ArmBodyPart> getArms(BodyPartProvider provider) {
        return new Pair<>(
            (ArmBodyPart)provider.getOrThrow(BuiltInParts.LEFT_ARM),
            (ArmBodyPart)provider.getOrThrow(BuiltInParts.RIGHT_ARM)
        );
    }
    
    public static Pair<LegBodyPart, LegBodyPart> getLegs(BodyPartProvider provider) {
        return new Pair<>(
            (LegBodyPart)provider.getOrThrow(BuiltInParts.LEFT_LEG),
            (LegBodyPart)provider.getOrThrow(BuiltInParts.RIGHT_LEG)
        );
    }
    
    public static Pair<FootBodyPart, FootBodyPart> getFeet(BodyPartProvider provider) {
        return new Pair<>(
            (FootBodyPart)provider.getOrThrow(BuiltInParts.LEFT_FOOT),
            (FootBodyPart)provider.getOrThrow(BuiltInParts.RIGHT_FOOT)
        );
    }
}
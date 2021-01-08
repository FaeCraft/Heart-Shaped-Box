package io.github.faecraft.heartshapedbox.body;

import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Locale;

import static io.github.faecraft.heartshapedbox.body.BodyPartSide.LEFT;
import static io.github.faecraft.heartshapedbox.body.BodyPartSide.RIGHT;

public class BuiltInParts {
    // Head
    public static final Identifier HEAD = new Identifier("hsb:head");
    
    // Arms
    public static final Identifier LEFT_ARM = new Identifier("hsb:arm/" + LEFT.name().toLowerCase(Locale.ROOT));
    public static final Identifier RIGHT_ARM = new Identifier("hsb:arm/" + RIGHT.name().toLowerCase(Locale.ROOT));
    
    // Legs
    public static final Identifier LEFT_LEG = new Identifier("hsb:leg/" + LEFT.name().toLowerCase(Locale.ROOT));
    public static final Identifier RIGHT_LEG = new Identifier("hsb:leg/" + RIGHT.name().toLowerCase(Locale.ROOT));
    
    // Feet
    public static final Identifier LEFT_FOOT = new Identifier("hsb:foot/" + LEFT.name().toLowerCase(Locale.ROOT));
    public static final Identifier RIGHT_FOOT = new Identifier("hsb:foot/" + RIGHT.name().toLowerCase(Locale.ROOT));
    
    
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

package net.heartshapedbox.body;

import net.heartshapedbox.body.impl.FootBodyPart;
import net.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.util.Pair;

// Duck, make sure to add to this for each part
public interface BodyPartProvider {
    Pair<LegBodyPart, LegBodyPart> getLegs();
    
    Pair<FootBodyPart, FootBodyPart> getFeet();
}

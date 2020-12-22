package net.heartshapedbox.body;

import net.heartshapedbox.body.impl.LegsBodyPart;

// Duck, make sure to add to this for each part
public interface BodyPartProvider {
    LegsBodyPart getLegs();
}

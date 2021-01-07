package io.github.faecraft.heartshapedbox.body;

import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.HeadBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.util.Pair;

import java.util.ArrayList;

// Duck, make sure to add to this for each part
public interface BodyPartProvider {
    HeadBodyPart getHead();
    
    Pair<ArmBodyPart, ArmBodyPart> getArms();
    
    Pair<LegBodyPart, LegBodyPart> getLegs();
    
    Pair<FootBodyPart, FootBodyPart> getFeet();
    
    ArrayList<AbstractBodyPart> getAll();
    
    ArrayList<AbstractBodyPart> stateCopy();
    
    void setFrom(ArrayList<AbstractBodyPart> provider);
}

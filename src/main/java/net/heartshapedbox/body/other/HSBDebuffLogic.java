package net.heartshapedbox.body.other;

import net.heartshapedbox.body.BodyPartProvider;
import net.heartshapedbox.body.impl.FootBodyPart;
import net.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

public class HSBDebuffLogic {
    public static void debuffPlayer(ServerPlayerEntity playerEntity) {
        BodyPartProvider provider = (BodyPartProvider)playerEntity;
        
        // Check for broken legs/feet
        // Each broken part adds a slowness level
        Pair<LegBodyPart, LegBodyPart> legs = provider.getLegs();
        Pair<FootBodyPart, FootBodyPart> feet = provider.getFeet();
        int amp = -1;
        
        if (legs.getLeft().getHealth() <= 0) amp++;
        if (legs.getRight().getHealth() <= 0) amp++;
        if (feet.getLeft().getHealth() <= 0) amp++;
        if (feet.getLeft().getHealth() <= 0) amp++;
        
        if (amp > -1) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, amp, true, true));
        }
    }
}

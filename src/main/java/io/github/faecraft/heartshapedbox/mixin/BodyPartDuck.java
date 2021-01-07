package io.github.faecraft.heartshapedbox.mixin;

import com.mojang.authlib.GameProfile;
import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.HeadBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(PlayerEntity.class)
public abstract class BodyPartDuck implements BodyPartProvider {
    private final ArrayList<AbstractBodyPart> parts = new ArrayList<>();
    
    @Inject(method = "<init>", at = @At("HEAD"))
    public void addPartsOnInit(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci) {
        parts.add(new HeadBodyPart());
        parts.add(new ArmBodyPart(BodyPartSide.LEFT));
        parts.add(new ArmBodyPart(BodyPartSide.RIGHT));
        parts.add(new LegBodyPart(BodyPartSide.LEFT));
        parts.add(new LegBodyPart(BodyPartSide.RIGHT));
        parts.add(new FootBodyPart(BodyPartSide.LEFT));
        parts.add(new FootBodyPart(BodyPartSide.RIGHT));
    }
    
    @Override
    public ArrayList<AbstractBodyPart> getParts() {
        return parts;
    }
    
    @Override
    public ArrayList<AbstractBodyPart> stateCopy() {
        return null;
    }
    
    @Override
    public void setStateFrom(ArrayList<AbstractBodyPart> provider) {
    
    }
}

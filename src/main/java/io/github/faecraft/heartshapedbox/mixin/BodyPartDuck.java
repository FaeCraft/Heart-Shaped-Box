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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class BodyPartDuck implements BodyPartProvider {
    private final ArrayList<AbstractBodyPart> parts = new ArrayList<>();
    
    @Inject(method = "<init>*", at = @At("RETURN"))
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
    public Optional<AbstractBodyPart> maybeGet(Identifier identifier) {
        return parts.stream().filter(abstractBodyPart -> abstractBodyPart.getIdentifier().equals(identifier)).findFirst();
    }
    
    @Override
    public CompoundTag writeToTag() {
        CompoundTag out = new CompoundTag();
        getParts().iterator().forEachRemaining(abstractBodyPart -> abstractBodyPart.toTag(out));
        return out;
    }
    
    @Override
    public void readFromTag(CompoundTag tag) {
        CompoundTag data = tag.getCompound("hsb");
        for (String key : data.getKeys()) {
            Optional<AbstractBodyPart> optional = maybeGet(new Identifier(key));
            optional.ifPresent(abstractBodyPart -> abstractBodyPart.fromTag(data.getCompound(key)));
        }
    }
    
    @Override
    public AbstractBodyPart getOrThrow(Identifier identifier) {
        //noinspection OptionalGetWithoutIsPresent
        return maybeGet(identifier).get();
    }
}

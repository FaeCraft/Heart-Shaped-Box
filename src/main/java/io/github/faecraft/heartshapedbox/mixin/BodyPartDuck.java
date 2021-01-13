package io.github.faecraft.heartshapedbox.mixin;

import com.mojang.authlib.GameProfile;
import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import io.github.faecraft.heartshapedbox.body.BodyPartSide;
import io.github.faecraft.heartshapedbox.body.BuiltInParts;
import io.github.faecraft.heartshapedbox.body.impl.ArmBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.FootBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.HeadBodyPart;
import io.github.faecraft.heartshapedbox.body.impl.LegBodyPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class BodyPartDuck implements BodyPartProvider {
    private final HashMap<Identifier, AbstractBodyPart> parts = new HashMap<>();

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void addPartsOnInit(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci) {
        parts.put(BuiltInParts.HEAD, new HeadBodyPart(this.asEntity()));
        parts.put(BuiltInParts.LEFT_ARM, new ArmBodyPart(this.asEntity(), BodyPartSide.LEFT));
        parts.put(BuiltInParts.RIGHT_ARM, new ArmBodyPart(this.asEntity(), BodyPartSide.RIGHT));
        parts.put(BuiltInParts.LEFT_LEG, new LegBodyPart(this.asEntity(), BodyPartSide.LEFT));
        parts.put(BuiltInParts.RIGHT_LEG, new LegBodyPart(this.asEntity(), BodyPartSide.RIGHT));
        parts.put(BuiltInParts.LEFT_FOOT, new FootBodyPart(this.asEntity(), BodyPartSide.LEFT));
        parts.put(BuiltInParts.RIGHT_FOOT, new FootBodyPart(this.asEntity(), BodyPartSide.RIGHT));
    }

    public PlayerEntity asEntity() {
        return (PlayerEntity) (Object) this;
    }

    @Override
    public HashMap<Identifier, AbstractBodyPart> getPartsMap() {
        return parts;
    }

    @Override
    public ArrayList<AbstractBodyPart> getParts() {
        return new ArrayList<>(parts.values());
    }

    @Override
    public Optional<AbstractBodyPart> maybeGet(Identifier identifier) {
        return Optional.ofNullable(parts.get(identifier));
    }

    @Override
    public @Nullable AbstractBodyPart getOrNull(Identifier identifier) {
        return parts.get(identifier);
    }

    @Override
    public AbstractBodyPart getOrThrow(Identifier identifier) {
        //noinspection OptionalGetWithoutIsPresent
        return maybeGet(identifier).get();
    }

    @Override
    public CompoundTag writeToTag() {
        CompoundTag out = new CompoundTag();
        getParts().iterator().forEachRemaining(abstractBodyPart -> abstractBodyPart.toTag(out));
        return out;
    }

    @Override
    public void readFromTag(CompoundTag tag) {
        for (String key : tag.getKeys()) {
            Optional<AbstractBodyPart> optional = maybeGet(new Identifier(key));
            optional.ifPresent(abstractBodyPart -> abstractBodyPart.fromTag(tag.getCompound(key)));
        }
    }
}

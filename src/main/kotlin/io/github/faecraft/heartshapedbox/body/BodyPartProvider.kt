package io.github.faecraft.heartshapedbox.body

import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import java.util.*

/**
 * Duck, applied in BodyPartDuck mixin
 */
public interface BodyPartProvider {
    public val partsMap: HashMap<Identifier, AbstractBodyPart>
    public val parts: ArrayList<AbstractBodyPart>
    public fun writeToTag(): CompoundTag
    public fun readFromTag(tag: CompoundTag)
    public fun maybeGet(identifier: Identifier): Optional<AbstractBodyPart>
    public fun getOrNull(identifier: Identifier): AbstractBodyPart?
    public fun getOrThrow(identifier: Identifier): AbstractBodyPart
}

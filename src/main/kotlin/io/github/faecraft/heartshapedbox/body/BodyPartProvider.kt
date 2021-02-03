package io.github.faecraft.heartshapedbox.body

import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import java.util.*

/**
 * Duck, applied in BodyPartDuck mixin
 */
interface BodyPartProvider {
    val partsMap: HashMap<Identifier, AbstractBodyPart>
    val parts: ArrayList<AbstractBodyPart>
    fun writeToTag(): CompoundTag
    fun readFromTag(tag: CompoundTag)
    fun maybeGet(identifier: Identifier): Optional<AbstractBodyPart>
    fun getOrNull(identifier: Identifier): AbstractBodyPart?
    fun getOrThrow(identifier: Identifier): AbstractBodyPart
}

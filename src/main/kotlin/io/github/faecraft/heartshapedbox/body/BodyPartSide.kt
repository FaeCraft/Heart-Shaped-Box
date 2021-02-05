package io.github.faecraft.heartshapedbox.body

import java.util.*

internal interface LowerCaseEnumName {
    fun lowerName(): String
}

public enum class BodyPartSide : LowerCaseEnumName {
    CENTER, LEFT, RIGHT;

    override fun lowerName(): String = name.toLowerCase(Locale.ROOT)
}

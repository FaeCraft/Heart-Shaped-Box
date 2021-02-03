package io.github.faecraft.heartshapedbox.body

import java.util.Locale

internal interface LowerCaseEnumName {
    fun lowerName(): String
}

enum class BodyPartSide : LowerCaseEnumName {
    CENTER, LEFT, RIGHT;

    override fun lowerName(): String {
        return name.toLowerCase(Locale.ROOT)
    }
}

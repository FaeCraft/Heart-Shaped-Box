package io.github.faecraft.heartshapedbox.body

import io.github.faecraft.heartshapedbox.body.lowerCaseEnumName
import java.util.Locale

internal interface lowerCaseEnumName {
    fun lowerName(): String
}

enum class BodyPartSide : lowerCaseEnumName {
    CENTER, LEFT, RIGHT;

    override fun lowerName(): String {
        return name.toLowerCase(Locale.ROOT)
    }
}

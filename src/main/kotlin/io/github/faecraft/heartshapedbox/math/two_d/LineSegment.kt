package io.github.faecraft.heartshapedbox.math.two_d

import net.minecraft.util.math.Vec2f
import java.util.*

data class LineSegment(val a: Vec2f, val b: Vec2f) {
    fun intersectFromLine(line: Line, checkY: Boolean): Optional<Vec2f> {
        return if (!checkY) {
            val aRes = line.solveForX(a.y.toDouble())
            val bRes = line.solveForX(b.y.toDouble())
            if (isNotValid(aRes) || isNotValid(bRes)) {
                return Optional.empty()
            }
            if (hasSignChange(a.x.toDouble(), aRes, bRes)) {
                val endA = Vec2f(aRes.toFloat(), a.y)
                val endB = Vec2f(bRes.toFloat(), b.y)
                val a1 = (a.y - b.y).toDouble()
                val b1 = (b.x - a.x).toDouble()
                val c1 = a1 * b.x + b1 * b.y
                val a2 = (endB.y - endA.y).toDouble()
                val b2 = (endA.x - endB.x).toDouble()
                val c2 = a2 * endA.x + b2 * endA.y
                val delta = a1 * b2 - a2 * b1
                Optional.of(
                    Vec2f(
                        ((b2 * c1 - b1 * c2) / delta).toFloat(),
                        ((a1 * c2 - a2 * c1) / delta).toFloat()
                    )
                )
            } else {
                Optional.empty()
            }
        } else {
            val aRes = line.solveForY(a.x.toDouble())
            val bRes = line.solveForY(b.x.toDouble())
            if (isNotValid(aRes) || isNotValid(bRes)) {
                return Optional.empty()
            }
            if (hasSignChange(a.y.toDouble(), aRes, bRes)) {
                val endA = Vec2f(a.x, aRes.toFloat())
                val endB = Vec2f(b.x, bRes.toFloat())
                val a1 = (a.y - b.y).toDouble()
                val b1 = (b.x - a.x).toDouble()
                val c1 = a1 * b.x + b1 * b.y
                val a2 = (endB.y - endA.y).toDouble()
                val b2 = (endA.x - endB.x).toDouble()
                val c2 = a2 * endA.x + b2 * endA.y
                val delta = a1 * b2 - a2 * b1
                Optional.of(
                    Vec2f(
                        ((b2 * c1 - b1 * c2) / delta).toFloat(),
                        ((a1 * c2 - a2 * c1) / delta).toFloat()
                    )
                )
            } else {
                Optional.empty()
            }
        }
    }

    private fun hasSignChange(base: Double, a: Double, b: Double): Boolean {
        var mutA = a
        var mutB = b
        mutA -= base
        mutB -= base
        return mutA >= 0 && mutB <= 0 || mutA <= 0 && mutB >= 0
    }

    private fun isNotValid(value: Double): Boolean {
        return java.lang.Double.isNaN(value) || java.lang.Double.isInfinite(value)
    }
}

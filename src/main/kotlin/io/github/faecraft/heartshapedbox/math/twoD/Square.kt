package io.github.faecraft.heartshapedbox.math.twoD

import io.github.faecraft.heartshapedbox.util.QuadSame
import net.minecraft.util.math.Vec2f
import java.util.*

public data class Square(val min: Vec2f, val max: Vec2f) {
    private val a: LineSegment = LineSegment(min, Vec2f(min.x, max.y))
    private val b: LineSegment = LineSegment(Vec2f(min.x, max.y), max)
    private val c: LineSegment = LineSegment(Vec2f(max.x, min.y), max)
    private val d: LineSegment = LineSegment(min, Vec2f(max.x, min.y))

    public fun splitFromLine(line: Line): Pair<QuadSame<Vec2f>, QuadSame<Vec2f>> {
        val aRes: Optional<Vec2f> = a.intersectFromLine(line, false)

        return if (aRes.isPresent) {
            val cRes: Optional<Vec2f> = c.intersectFromLine(line, false)

            QuadSame(aRes.get(), b.a, max, cRes.getOrIllegal()) to
                    QuadSame(aRes.get(), min, d.b, cRes.getOrIllegal())
        } else {
            val bRes: Optional<Vec2f> = b.intersectFromLine(line, true)
            val dRes: Optional<Vec2f> = d.intersectFromLine(line, true)

            QuadSame(bRes.getOrIllegal(), b.a, min, dRes.getOrIllegal()) to
                    QuadSame(bRes.getOrIllegal(), max, d.b, dRes.getOrIllegal())
        }
    }

    private fun <T : Any?> Optional<T>.getOrIllegal(): T =
        this.orElseThrow { IllegalStateException("Optional may not return null at this stage") }
}

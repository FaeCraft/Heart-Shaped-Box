package io.github.faecraft.heartshapedbox.math.two_d

import net.minecraft.util.math.Vec2f
import java.util.*

data class Square(val min: Vec2f, val max: Vec2f) {
    private val a: LineSegment = LineSegment(min, Vec2f(min.x, max.y))
    private val b: LineSegment = LineSegment(Vec2f(min.x, max.y), max)
    private val c: LineSegment = LineSegment(Vec2f(max.x, min.y), max)
    private val d: LineSegment = LineSegment(min, Vec2f(max.x, min.y))

    fun splitFromLine(line: Line): Pair<Array<Vec2f>, Array<Vec2f>> {
        val aRes: Optional<Vec2f> = a.intersectFromLine(line, false)
        return if (aRes.isPresent) {
            val cRes: Optional<Vec2f> = c.intersectFromLine(line, false)
            Pair(arrayOf(
                aRes.get(),
                b.a,
                max,
                cRes.getOrIllegal()
            ), arrayOf(
                aRes.get(),
                min,
                d.b,
                cRes.getOrIllegal()
            ))
        } else {
            val bRes: Optional<Vec2f> = b.intersectFromLine(line, true)
            val dRes: Optional<Vec2f> = d.intersectFromLine(line, true)
            Pair(arrayOf(
                bRes.getOrIllegal(),
                b.a,
                min,
                dRes.getOrIllegal()
            ), arrayOf(
                bRes.getOrIllegal(),
                max,
                d.b,
                dRes.getOrIllegal()
            ))
        }
    }

    private fun <T: Any?> Optional<T>.getOrIllegal(): T {
        return this.orElseThrow { IllegalStateException() }
    }
}

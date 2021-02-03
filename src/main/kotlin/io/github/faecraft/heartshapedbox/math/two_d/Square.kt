package io.github.faecraft.heartshapedbox.math.two_d

import net.minecraft.util.math.Vec2f
import java.util.*

class Square(a: Vec2f, b: Vec2f) {
    private val a: LineSegment = LineSegment(a, Vec2f(a.x, b.y))
    private val b: LineSegment = LineSegment(Vec2f(a.x, b.y), b)
    private val c: LineSegment = LineSegment(Vec2f(b.x, a.y), b)
    private val d: LineSegment = LineSegment(a, Vec2f(b.x, a.y))

    private val min: Vec2f = a
    private val max: Vec2f = b

    fun splitFromLine(line: Line): Pair<Array<Vec2f>, Array<Vec2f>> {
        val aRes: Optional<Vec2f> = a.intersectFromLine(line, false)
        return if (aRes.isPresent) {
            val cRes: Optional<Vec2f> = c.intersectFromLine(line, false)
            Pair(arrayOf(
                aRes.get(),
                b.a,
                max,
                cRes.orElseThrow { IllegalStateException() }
            ), arrayOf(
                aRes.get(),
                min,
                d.b,
                cRes.orElseThrow { IllegalStateException() }
            ))
        } else {
            val bRes: Optional<Vec2f> = b.intersectFromLine(line, true)
            val dRes: Optional<Vec2f> = d.intersectFromLine(line, true)
            Pair(arrayOf(
                bRes.orElseThrow { IllegalStateException() },
                b.a,
                min,
                dRes.orElseThrow { IllegalStateException() }
            ), arrayOf(
                bRes.orElseThrow { IllegalStateException() },
                max,
                d.b,
                dRes.orElseThrow { IllegalStateException() }
            ))
        }
    }
}

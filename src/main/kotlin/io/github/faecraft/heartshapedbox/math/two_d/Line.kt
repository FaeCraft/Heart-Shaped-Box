package io.github.faecraft.heartshapedbox.math.two_d

import net.minecraft.util.math.Vec2f

class Line(val start: Vec2f, val slope: Double) {
    val b: Float
    fun solveForX(y: Double): Double {
        return (y - b) / slope
    }

    fun solveForY(x: Double): Double {
        return slope * x + b
    }

    init {
        b = (start.y - slope * start.x).toFloat()
    }
}

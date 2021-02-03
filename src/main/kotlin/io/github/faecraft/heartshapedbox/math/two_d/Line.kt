package io.github.faecraft.heartshapedbox.math.two_d

import net.minecraft.util.math.Vec2f

data class Line(private val start: Vec2f, private val slope: Double) {
    private val b: Float = (start.y - slope * start.x).toFloat()

    fun solveForX(y: Double): Double {
        return (y - b) / slope
    }

    fun solveForY(x: Double): Double {
        return slope * x + b
    }
}

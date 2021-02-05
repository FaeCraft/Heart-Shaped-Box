package io.github.faecraft.heartshapedbox.math.twoD

import net.minecraft.util.math.Vec2f

public data class Line(private val start: Vec2f, private val slope: Double) {
    private val b: Float = (start.y - slope * start.x).toFloat()

    public fun solveForX(y: Double): Double = (y - b) / slope
    public fun solveForY(x: Double): Double = slope * x + b
}

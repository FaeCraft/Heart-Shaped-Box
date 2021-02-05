package io.github.faecraft.heartshapedbox.math

import net.minecraft.util.math.Vec3d

public data class Quad(val point1: Vec3d, val point2: Vec3d, val point3: Vec3d, val point4: Vec3d) {
    val points: MutableList<Vec3d> = mutableListOf(point1, point2, point3, point4)
}

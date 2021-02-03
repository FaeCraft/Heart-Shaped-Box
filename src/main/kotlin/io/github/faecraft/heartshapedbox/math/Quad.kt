package io.github.faecraft.heartshapedbox.math

import net.minecraft.util.math.Vec3d

class Quad(point1: Vec3d, point2: Vec3d, point3: Vec3d, point4: Vec3d) {
    val points = mutableListOf<Vec3d>()

    init {
        points.add(point1)
        points.add(point2)
        points.add(point3)
        points.add(point4)
    }
}

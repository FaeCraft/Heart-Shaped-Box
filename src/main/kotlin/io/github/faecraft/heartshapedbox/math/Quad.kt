package io.github.faecraft.heartshapedbox.math

import net.minecraft.util.math.Vec3d

class Quad(point1: Vec3d, point2: Vec3d, point3: Vec3d, point4: Vec3d) {
    val points = arrayOf<Vec3d>()

    init {
        points[0] = point1
        points[1] = point2
        points[2] = point3
        points[3] = point4
    }
}

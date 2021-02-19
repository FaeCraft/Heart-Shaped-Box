package io.github.faecraft.heartshapedbox.math

import io.github.faecraft.heartshapedbox.util.QuadSame
import net.minecraft.util.math.Vec3d

public data class Quad(val point1: Vec3d, val point2: Vec3d, val point3: Vec3d, val point4: Vec3d) {
    val points: QuadSame<Vec3d> = QuadSame(point1, point2, point3, point4)
}

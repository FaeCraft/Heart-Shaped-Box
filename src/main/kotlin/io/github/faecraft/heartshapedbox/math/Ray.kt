package io.github.faecraft.heartshapedbox.math

import net.minecraft.util.math.Vec3d
data class Ray(val start: Vec3d, var direction: Vec3d) {
    init {
        direction = direction.normalize()
    }

    private fun intersectsQuad(quad: Quad): Boolean {
        val a = MollerTrumbore.rayIntersectsTriangle(
            start,
            direction,
            Triangle(quad.points[0], quad.points[1], quad.points[2])
        )
        if (a.isPresent) {
            return true
        }
        val b = MollerTrumbore.rayIntersectsTriangle(
            start,
            direction,
            Triangle(quad.points[0], quad.points[3], quad.points[2])
        )
        return b.isPresent
    }

    fun intersectsBox(box: FlexBox): Boolean {
        return box.quads.any { intersectsQuad(it) }
    }
}


package io.github.faecraft.heartshapedbox.math

import net.minecraft.util.math.Vec3d

public data class Ray(val start: Vec3d, val direction: Vec3d) {
    val normalizedDirection: Vec3d = direction.normalize()

    private fun intersectsQuad(quad: Quad): Boolean {
        val a = MollerTrumbore.rayIntersectsTriangle(
            start,
            normalizedDirection,
            Triangle(quad.points[0], quad.points[1], quad.points[2])
        )

        if (a.isPresent) {
            return true
        }

        val b = MollerTrumbore.rayIntersectsTriangle(
            start,
            normalizedDirection,
            Triangle(quad.points[0], quad.points[3], quad.points[2])
        )

        return b.isPresent
    }

    public fun intersectsBox(box: FlexBox): Boolean =
        box.quads.any { intersectsQuad(it) }
}


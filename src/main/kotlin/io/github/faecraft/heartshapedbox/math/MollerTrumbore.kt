package io.github.faecraft.heartshapedbox.math

import net.minecraft.util.math.Vec3d
import java.util.*

// Slightly adapted from
// https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm
// For compatibility with Minecraft
object MollerTrumbore {
    fun rayIntersectsTriangle(rayOrigin: Vec3d, rayVector: Vec3d, inTriangle: Triangle): Optional<Vec3d> {
        val epsilon = 0.0000001
        val vertex0 = inTriangle.vec1
        val vertex1 = inTriangle.vec2
        val vertex2 = inTriangle.vec3

        val edge1: Vec3d = vertex1.subtract(vertex0)
        val edge2: Vec3d = vertex2.subtract(vertex0)

        val h: Vec3d = rayVector.crossProduct(edge2)
        val a: Double = edge1.dotProduct(h)

        if (a > -epsilon && a < epsilon) return Optional.empty() // This ray is parallel to this triangle.

        val f: Double = 1.0 / a
        val s: Vec3d = rayOrigin.subtract(vertex0)
        val u: Double = f * s.dotProduct(h)
        if (u < 0.0 || u > 1.0) return Optional.empty()

        val q: Vec3d = s.crossProduct(edge1)
        val v: Double = f * rayVector.dotProduct(q)
        if (v < 0.0 || u + v > 1.0) return Optional.empty()

        // At this stage we can compute t to find out where the intersection point is on the line.
        val t = f * edge2.dotProduct(q)
        return if (t > epsilon) { // ray intersection
            Optional.of(rayOrigin.add(rayVector.multiply(t)))
        } else { // This means that there is a line intersection but not a ray intersection.
            Optional.empty()
        }
    }
}

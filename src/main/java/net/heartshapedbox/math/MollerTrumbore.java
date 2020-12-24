package net.heartshapedbox.math;

import net.minecraft.util.math.Vec3d;

import java.util.Optional;

// Slightly adapted from
// https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm
// For compatibility with Minecraft
public class MollerTrumbore {
    public static Optional<Vec3d> rayIntersectsTriangle(Vec3d rayOrigin, Vec3d rayVector, Triangle inTriangle) {
        double EPSILON = 0.0000001;
        
        Vec3d vertex0 = inTriangle.vec1;
        Vec3d vertex1 = inTriangle.vec2;
        Vec3d vertex2 = inTriangle.vec3;
        Vec3d edge1, edge2, h, s, q;
        double a,f,u,v;
        edge1 = vertex1.subtract(vertex0);
        edge2 = vertex2.subtract(vertex0);
        h = rayVector.crossProduct(edge2);
        a = edge1.dotProduct(h);
        if (a > -EPSILON && a < EPSILON)
            return Optional.empty();    // This ray is parallel to this triangle.
        f = 1.0/a;
        s = rayOrigin.subtract(vertex0);
        u = f * s.dotProduct(h);
        if (u < 0.0 || u > 1.0)
            return Optional.empty();
        q = s.crossProduct(edge1);
        v = f * rayVector.dotProduct(q);
        if (v < 0.0 || u + v > 1.0)
            return Optional.empty();
        // At this stage we can compute t to find out where the intersection point is on the line.
        double t = f * edge2.dotProduct(q);
        if (t > EPSILON) // ray intersection
        {
            return Optional.of(rayOrigin.add(rayVector.multiply(t)));
        }
        else // This means that there is a line intersection but not a ray intersection.
            return Optional.empty();
    }
}

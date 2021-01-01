package io.github.faecraft.heartshapedbox.math;

import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Optional;

public class Ray {
    public final Vec3d start;
    public final Vec3d direction;
    
    public Ray(Vec3d start, Vec3d direction) {
        this.start = start;
        this.direction = direction.normalize();
    }
    
    public boolean intersectsQuad(Quad quad) {
        Optional<Vec3d> a = MollerTrumbore.rayIntersectsTriangle(
            start,
            direction,
            new Triangle(quad.points[0], quad.points[1], quad.points[2])
        );
        if (a.isPresent()) {
            return true;
        }
        
        Optional<Vec3d> b = MollerTrumbore.rayIntersectsTriangle(
            start,
            direction,
            new Triangle(quad.points[0], quad.points[3], quad.points[2])
        );
        return b.isPresent();
    }
    
    public boolean intersectsBox(FlexBox box) {
        return Arrays.stream(box.quads).anyMatch(this::intersectsQuad);
    }
}

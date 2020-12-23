package net.heartshapedbox.math;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Ray {
    Vec3d start;
    Vec3d direction;
    
    public Ray(Vec3d start, Vec3d direction) {
        this.start = start;
        this.direction = direction.normalize();
    }
    
    public boolean intersectsBox(Box box) {
        // Adapted from
        // https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-box-intersection
    
        double tmin = (box.minX - start.x) / direction.x;
        double tmax = (box.maxX - start.x) / direction.x;
    
        double tymin = (box.minY - start.y) / direction.y;
        double tymax = (box.maxY - start.y) / direction.y;
    
        if ((tmin > tymax) || (tymin > tmax))
            return false;
    
        if (tymin > tmin)
            tmin = tymin;
    
        if (tymax < tmax)
            tmax = tymax;
    
        double tzmin = (box.minZ - start.z) / direction.z;
        double tzmax = (box.maxZ - start.z) / direction.z;
    
        return (!(tmin > tzmax)) && (!(tzmin > tmax));
    }
}

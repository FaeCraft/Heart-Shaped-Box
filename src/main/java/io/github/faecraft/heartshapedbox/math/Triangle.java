package io.github.faecraft.heartshapedbox.math;

import net.minecraft.util.math.Vec3d;

public class Triangle {
    final Vec3d vec1;
    final Vec3d vec2;
    final Vec3d vec3;
    
    public Triangle(Vec3d v1, Vec3d v2, Vec3d v3) {
        this.vec1 = v1;
        this.vec2 = v2;
        this.vec3 = v3;
    }
}

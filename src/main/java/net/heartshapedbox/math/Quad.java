package net.heartshapedbox.math;

import net.minecraft.util.math.Vec3d;

public class Quad {
    public Vec3d[] points = new Vec3d[4];
    
    public Quad(Vec3d point1, Vec3d point2, Vec3d point3, Vec3d point4) {
        points[0] = point1;
        points[1] = point2;
        points[2] = point3;
        points[3] = point4;
    }
}

package net.heartshapedbox.math;

import net.minecraft.util.math.Vec3d;

// box with a flex
// how perplex
public class FlexBox {
    Quad[] quads = new Quad[6];
    
    public FlexBox(Vec3d a, Vec3d b, Vec3d c, Vec3d d, Vec3d e, Vec3d f, Vec3d g, Vec3d h) {
        quads[0] = new Quad(a, b, c, d);
        quads[1] = new Quad(e, f, g, h);
        quads[2] = new Quad(a, e, h, d);
        quads[3] = new Quad(b, f, e, a);
        quads[4] = new Quad(c, g, f, b);
        quads[5] = new Quad(d, h, g, c);
    }
    
    public static FlexBox zero() {
        return new FlexBox(Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO);
    }
}

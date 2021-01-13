package io.github.faecraft.heartshapedbox.math;

import net.minecraft.util.math.Vec3d;

// box with a flex
// how perplex
public class FlexBox {
    public final Quad[] quads = new Quad[6];
    
    // Not the actual center, but a good enough approximation for what its used for
    public final Vec3d roughCenter;
    
    public FlexBox(Vec3d a, Vec3d b, Vec3d c, Vec3d d, Vec3d e, Vec3d f, Vec3d g, Vec3d h) {
        quads[0] = new Quad(a, b, c, d);
        quads[1] = new Quad(e, f, g, h);
        quads[2] = new Quad(a, e, h, d);
        quads[3] = new Quad(b, f, e, a);
        quads[4] = new Quad(c, g, f, b);
        quads[5] = new Quad(d, h, g, c);
        
        roughCenter = a.add(h.subtract(a).multiply(0.5));
    }
    
    public FlexBox(Vec3d a, Vec3d b, Vec3d c, Vec3d d, double height) {
        Vec3d e = a.add(0, height, 0);
        Vec3d f = b.add(0, height, 0);
        Vec3d g = c.add(0, height, 0);
        Vec3d h = d.add(0, height, 0);
        
        quads[0] = new Quad(a, b, c, d);
        quads[1] = new Quad(e, f, g, h);
        quads[2] = new Quad(a, e, h, d);
        quads[3] = new Quad(b, f, e, a);
        quads[4] = new Quad(c, g, f, b);
        quads[5] = new Quad(d, h, g, c);
    
        roughCenter = a.add(h.subtract(a).multiply(0.5));
    }

    public FlexBox(Quad a, Quad b, Quad c, Quad d, Quad e, Quad f, Vec3d roughCenter) {
        quads[0] = a;
        quads[1] = b;
        quads[2] = c;
        quads[3] = d;
        quads[4] = e;
        quads[5] = f;

        this.roughCenter = roughCenter;
    }
    
    public static FlexBox zero() {
        return new FlexBox(Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO, Vec3d.ZERO);
    }
}

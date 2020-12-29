package net.heartshapedbox.math.two_d;

import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;

import java.util.Optional;

public class Square {
    LineSegment a;
    LineSegment b;
    LineSegment c;
    LineSegment d;
    
    Vec2f min;
    Vec2f max;
    
    /**
     *          b      b(p)
     *     -----------
     *     |         |
     *   a |         |  c
     *     |         |
     *     -----------
     *  a(p)   d
     */
    public Square(Vec2f a, Vec2f b) {
        this.min = a;
        this.max = b;
        
        this.a = new LineSegment(a, new Vec2f(a.x, b.y));
        this.b = new LineSegment(new Vec2f(a.x, b.y), b);
        this.c = new LineSegment(new Vec2f(b.x, a.y), b);
        this.d = new LineSegment(a, new Vec2f(b.x, a.y));
    }
    
    public Pair<Vec2f[], Vec2f[]> splitFromLine(Line line) {
        Optional<Vec2f> aRes = a.intersectFromLine(line, false);
        if (aRes.isPresent()) {
            Optional<Vec2f> cRes = c.intersectFromLine(line, false);
            if (!cRes.isPresent()) {
                throw new IllegalStateException("cRes did not result in an intersect from the provided line, unable to continue.");
            }
            
            return new Pair<>(
                new Vec2f[]{
                    aRes.get(),
                    a.b,
                    max,
                    cRes.get()
                },
                new Vec2f[]{
                    aRes.get(),
                    min,
                    d.b,
                    cRes.get()
                }
            );
        } else {
            Optional<Vec2f> bRes = b.intersectFromLine(line, true);
            Optional<Vec2f> dRes = d.intersectFromLine(line, true);
            if (!bRes.isPresent() || !dRes.isPresent()) {
                throw new IllegalStateException("Either bRes or dRes did not result in an intersect from the provided line, unable to continue.");
            }
    
            return new Pair<>(
                new Vec2f[]{
                    bRes.get(),
                    b.a,
                    min,
                    dRes.get()
                },
                new Vec2f[]{
                    bRes.get(),
                    max,
                    d.b,
                    dRes.get()
                }
            );
        }
    }
}

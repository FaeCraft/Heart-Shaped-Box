package io.github.faecraft.heartshapedbox.math.two_d;

import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;

import java.util.Optional;

public class Square {
    final LineSegment a;
    final LineSegment b;
    final LineSegment c;
    final LineSegment d;
    
    final Vec2f min;
    final Vec2f max;
    
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
            
            return new Pair<>(
                new Vec2f[]{
                    aRes.get(),
                    b.a,
                    max,
                    cRes.orElseThrow(IllegalStateException::new)
                },
                new Vec2f[]{
                    aRes.get(),
                    min,
                    d.b,
                    cRes.orElseThrow(IllegalStateException::new)
                }
            );
        } else {
            Optional<Vec2f> bRes = b.intersectFromLine(line, true);
            Optional<Vec2f> dRes = d.intersectFromLine(line, true);
    
            return new Pair<>(
                new Vec2f[]{
                    bRes.orElseThrow(IllegalStateException::new),
                    b.a,
                    min,
                    dRes.orElseThrow(IllegalStateException::new)
                },
                new Vec2f[]{
                    bRes.orElseThrow(IllegalStateException::new),
                    max,
                    d.b,
                    dRes.orElseThrow(IllegalStateException::new)
                }
            );
        }
    }
}

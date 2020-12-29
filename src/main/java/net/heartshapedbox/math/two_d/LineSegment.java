package net.heartshapedbox.math.two_d;

import net.minecraft.util.math.Vec2f;

import java.util.Optional;

public class LineSegment {
    Vec2f a;
    Vec2f b;
    
    public LineSegment(Vec2f a, Vec2f b) {
        this.a = a;
        this.b = b;
    }
    
    public Optional<Vec2f> intersectFromLine(Line line, boolean checkY) {
        if (!checkY) {
            double aRes = line.solveForX(a.y);
            double bRes = line.solveForX(b.y);
            if (isNotValid(aRes) || isNotValid(bRes)) {
                return Optional.empty();
            }
            
            if (hasSignChange(a.x, aRes, bRes)) {
                Vec2f endA = new Vec2f((float)aRes, a.y);
                Vec2f endB = new Vec2f((float)bRes, b.y);
                
                double a1 = a.y - b.y;
                double b1 = b.x - a.x;
                double c1 = a1 * b.x + b1 * b.y;
    
                double a2 = endB.y - endA.y;
                double b2 = endA.x - endB.x;
                double c2 = a2 * endA.x + b2 * endA.y;
    
                double delta = a1 * b2 - a2 * b1;
                return Optional.of(new Vec2f((float) ((b2 * c1 - b1 * c2) / delta), (float) ((a1 * c2 - a2 * c1) / delta)));
            } else {
                return Optional.empty();
            }
        } else {
            double aRes = line.solveForY(a.x);
            double bRes = line.solveForY(b.x);
            if (isNotValid(aRes) || isNotValid(bRes)) {
                return Optional.empty();
            }
    
            if (hasSignChange(a.y, aRes, bRes)) {
                Vec2f endA = new Vec2f(a.x, (float)aRes);
                Vec2f endB = new Vec2f(b.x, (float)bRes);
        
                double a1 = a.y - b.y;
                double b1 = b.x - a.x;
                double c1 = a1 * b.x + b1 * b.y;
        
                double a2 = endB.y - endA.y;
                double b2 = endA.x - endB.x;
                double c2 = a2 * endA.x + b2 * endA.y;
        
                double delta = a1 * b2 - a2 * b1;
                return Optional.of(new Vec2f((float) ((b2 * c1 - b1 * c2) / delta), (float) ((a1 * c2 - a2 * c1) / delta)));
            } else {
                return Optional.empty();
            }
        }
    }
    
    private boolean hasSignChange(double base, double a, double b) {
        a -= base;
        b -= base;
        return (a >= 0 && b <= 0) || (a <= 0 && b >= 0);
    }
    
    private boolean isNotValid(double value) {
        return Double.isNaN(value) || Double.isInfinite(value);
    }
}

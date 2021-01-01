package io.github.faecraft.heartshapedbox.math.two_d;

import net.minecraft.util.math.Vec2f;

public class Line {
    final Vec2f start;
    final double slope;
    
    final float b;
    
    public Line(Vec2f start, double slope) {
        this.start = start;
        this.slope = slope;
        this.b = (float)(start.y - (slope * start.x));
    }
    
    public double solveForX(double y) {
        return (y - b) / slope;
    }
    
    public double solveForY(double x) {
        return (slope * x) + b;
    }
}

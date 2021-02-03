package io.github.faecraft.heartshapedbox.math

import net.minecraft.util.math.Vec3d
import io.github.faecraft.heartshapedbox.math.FlexBox

// box with a flex
// how perplex
class FlexBox {
    val quads = mutableListOf<Quad>()

    // Not the actual center, but a good enough approximation for what its used for
    val roughCenter: Vec3d

    constructor(a: Vec3d, b: Vec3d, c: Vec3d, d: Vec3d, e: Vec3d, f: Vec3d, g: Vec3d, h: Vec3d) {
        quads.add(Quad(a, b, c, d))
        quads.add(Quad(e, f, g, h))
        quads.add(Quad(a, e, h, d))
        quads.add(Quad(b, f, e, a))
        quads.add(Quad(c, g, f, b))
        quads.add(Quad(d, h, g, c))
        roughCenter = a.add(h.subtract(a).multiply(0.5))
    }

    constructor(a: Vec3d, b: Vec3d, c: Vec3d, d: Vec3d, height: Double) {
        val e = a.add(0.0, height, 0.0)
        val f = b.add(0.0, height, 0.0)
        val g = c.add(0.0, height, 0.0)
        val h = d.add(0.0, height, 0.0)
        quads.add(Quad(a, b, c, d))
        quads.add(Quad(e, f, g, h))
        quads.add(Quad(a, e, h, d))
        quads.add(Quad(b, f, e, a))
        quads.add(Quad(c, g, f, b))
        quads.add(Quad(d, h, g, c))
        roughCenter = a.add(h.subtract(a).multiply(0.5))
    }

    constructor(a: Quad, b: Quad, c: Quad, d: Quad, e: Quad, f: Quad, roughCenter: Vec3d) {
        quads.add(a)
        quads.add(b)
        quads.add(c)
        quads.add(d)
        quads.add(e)
        quads.add(f)
        this.roughCenter = roughCenter
    }

    companion object {
        fun zero(): FlexBox {
            return FlexBox(
                Vec3d.ZERO,
                Vec3d.ZERO,
                Vec3d.ZERO,
                Vec3d.ZERO,
                Vec3d.ZERO,
                Vec3d.ZERO,
                Vec3d.ZERO,
                Vec3d.ZERO
            )
        }
    }
}

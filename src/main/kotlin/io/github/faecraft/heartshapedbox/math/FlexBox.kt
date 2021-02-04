package io.github.faecraft.heartshapedbox.math

import net.minecraft.util.math.Vec3d

// box with a flex
// how perplex
data class FlexBox(
    val a: Quad,
    val b: Quad,
    val c: Quad,
    val d: Quad,
    val e: Quad,
    val f: Quad,
    val roughCenter: Vec3d
) {
    val quads = mutableListOf<Quad>()

    init {
        quads.addAll(listOf(a, b, c, d, e, f))
    }

    constructor(a: Vec3d, b: Vec3d, c: Vec3d, d: Vec3d, e: Vec3d, f: Vec3d, g: Vec3d, h: Vec3d) : this(
        Quad(a, b, c, d),
        Quad(e, f, g, h),
        Quad(a, e, h, d),
        Quad(b, f, e, a),
        Quad(c, g, f, b),
        Quad(d, h, g, c),
        a.add(h.subtract(a).multiply(0.5))
    )

    constructor(a: Vec3d, b: Vec3d, c: Vec3d, d: Vec3d, height: Double) : this(
        Quad(a, b, c, d),
        Quad(a.up(height), b.up(height), c.up(height), d.up(height)),
        Quad(a, a.up(height), d.up(height), d),
        Quad(b, b.up(height), a.up(height), a),
        Quad(c, c.up(height), b.up(height), b),
        Quad(d, d.up(height), c.up(height), c),
        a.add(d.up(height).subtract(a).multiply(0.5))
    )


    companion object {
        private fun Vec3d.up(height: Double): Vec3d {
            return this.add(0.0, height, 0.0)
        }

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

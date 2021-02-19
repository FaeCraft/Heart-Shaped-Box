package io.github.faecraft.heartshapedbox.math

import io.github.faecraft.heartshapedbox.util.FlexboxBaseBuilder
import io.github.faecraft.heartshapedbox.util.raise
import net.minecraft.util.math.Vec3d

// box with a flex
// how perplex
public data class FlexBox(
    val a: Quad,
    val b: Quad,
    val c: Quad,
    val d: Quad,
    val e: Quad,
    val f: Quad,
    val roughCenter: Vec3d
) {
    val quads: MutableList<Quad> = mutableListOf()

    init {
        quads.addAll(listOf(a, b, c, d, e, f))
    }

    @Suppress("MagicNumber")
    public constructor(a: Vec3d, b: Vec3d, c: Vec3d, d: Vec3d, e: Vec3d, f: Vec3d, g: Vec3d, h: Vec3d) : this(
        Quad(a, b, c, d),
        Quad(e, f, g, h),
        Quad(a, e, h, d),
        Quad(b, f, e, a),
        Quad(c, g, f, b),
        Quad(d, h, g, c),

        a.add(h.subtract(a).multiply(0.5))
    )

    @Suppress("MagicNumber")
    public constructor(a: Vec3d, b: Vec3d, c: Vec3d, d: Vec3d, height: Double) : this(
        Quad(a, b, c, d),
        Quad(a.raise(height), b.raise(height), c.raise(height), d.raise(height)),
        Quad(a, a.raise(height), d.raise(height), d),
        Quad(b, b.raise(height), a.raise(height), a),
        Quad(c, c.raise(height), b.raise(height), b),
        Quad(d, d.raise(height), c.raise(height), c),

        a.add(d.raise(height).subtract(a).multiply(0.5))
    )

    public constructor(builder: FlexboxBaseBuilder, height: Double) : this(
        builder.component1(),
        builder.component2(),
        builder.component3(),
        builder.component4(),
        height
    )

    public companion object {
        public val ZERO: FlexBox = FlexBox(
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

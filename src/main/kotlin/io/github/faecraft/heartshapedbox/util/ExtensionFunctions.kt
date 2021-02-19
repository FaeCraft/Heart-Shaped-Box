package io.github.faecraft.heartshapedbox.util

import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d

public fun Vec2f.raise(height: Double): Vec3d = Vec3d(x.toDouble(), height, y.toDouble())

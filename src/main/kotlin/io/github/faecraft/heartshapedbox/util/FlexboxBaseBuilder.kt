package io.github.faecraft.heartshapedbox.util

import io.github.faecraft.heartshapedbox.body.BodyPartSide
import io.github.faecraft.heartshapedbox.body.BodyPartSide.*
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d

public class FlexboxBaseBuilder(
    baseHeight: Double,
    leftSet: QuadSame<Vec2f>,
    rightSet: QuadSame<Vec2f>,
    buildType: BuildType
) {
    private var o1: Vec3d
    private var o2: Vec3d
    private var o3: Vec3d
    private var o4: Vec3d

    init {
        when (buildType) {
            BuildType.FULL_BOX -> {
                o1 = leftSet.two.raise(baseHeight)
                o2 = leftSet.one.raise(baseHeight)
                o3 = rightSet.one.raise(baseHeight)
                o4 = rightSet.two.raise(baseHeight)
            }
            BuildType.LEFT_ONLY -> {
                o1 = leftSet.zero.raise(baseHeight)
                o2 = leftSet.one.raise(baseHeight)
                o3 = leftSet.two.raise(baseHeight)
                o4 = leftSet.three.raise(baseHeight)
            }
            BuildType.RIGHT_ONLY -> {
                o1 = rightSet.zero.raise(baseHeight)
                o2 = rightSet.one.raise(baseHeight)
                o3 = rightSet.two.raise(baseHeight)
                o4 = rightSet.three.raise(baseHeight)
            }
        }
    }

    public operator fun component1(): Vec3d = o1
    public operator fun component2(): Vec3d = o2
    public operator fun component3(): Vec3d = o3
    public operator fun component4(): Vec3d = o4

    public enum class BuildType {
        FULL_BOX,
        LEFT_ONLY,
        RIGHT_ONLY;

        public companion object {
            public fun ofSide(side: BodyPartSide): BuildType {
                return when (side) {
                    CENTER -> FULL_BOX
                    LEFT -> LEFT_ONLY
                    RIGHT -> RIGHT_ONLY
                }
            }
        }
    }
}

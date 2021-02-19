package io.github.faecraft.heartshapedbox.util

public data class QuadSame<T>(val zero: T, val one: T, val two: T, val three: T) {
    public operator fun get(i: Int): T {
        return when(i) {
            0 -> zero
            1 -> one
            2 -> two
            3 -> three
            else -> throw IndexOutOfBoundsException("Attempting to access index $i")
        }
    }
}

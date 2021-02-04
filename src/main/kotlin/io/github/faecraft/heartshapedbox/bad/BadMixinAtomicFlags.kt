package io.github.faecraft.heartshapedbox.bad

import java.util.concurrent.atomic.AtomicBoolean

/**
 * This class exists to have state for some mixins.
 *
 * Often I want to pass to some higher logic, but by default it is either impossible or doesn't work
 * so these flags control that state
 *
 * @author P03W
 */
object BadMixinAtomicFlags {
    @JvmField
    val callSuperDamage = AtomicBoolean()
    @JvmField
    val doNotPassDamageToCustomLogic = AtomicBoolean()
}

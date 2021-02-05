package io.github.faecraft.heartshapedbox.util

import kotlin.reflect.KProperty

/**
 * Property delegate that acts like lazy but is settable afterwards
 *
 * Not nullable because lateinit is used
 */
public class SettableLazy<T: Any>(public val lazyProvider: () -> T) {
    public var hasSetup: Boolean = false
    public lateinit var storedValue: T

    public operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (!hasSetup) {
            storedValue = lazyProvider()
            hasSetup = true
        }
        return storedValue
    }

    public operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        hasSetup = true
        storedValue = value
    }
}

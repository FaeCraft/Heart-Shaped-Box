package io.github.faecraft.heartshapedbox.util

import kotlin.reflect.KProperty

/**
 * Property delegate that acts like lazy but is settable afterwards
 *
 * Not nullable because lateinit is used
 */
class SettableLazy<T: Any>(val lazyProvider: () -> T) {
    var hasSetup = false
    lateinit var storedValue: T

    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (!hasSetup) {
            storedValue = lazyProvider()
            hasSetup = true
        }
        return storedValue
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        hasSetup = true
        storedValue = value
    }
}

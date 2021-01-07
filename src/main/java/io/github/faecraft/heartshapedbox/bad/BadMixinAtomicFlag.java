package io.github.faecraft.heartshapedbox.bad;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class exists to have state for some mixins
 *
 * LivingEntity.damage needs to be called directly to preserve damage logic ordering
 * But java makes it very hard to do that easily, if not impossible
 * So a flag is used, that when set redirects calls to damage to super.damage() unless its on living entity
 *
 * No I don't like this solution, how could you tell?
 *
 * @author P03W
 */
public class BadMixinAtomicFlag {
    public static AtomicBoolean callSuperDamage = new AtomicBoolean();
}

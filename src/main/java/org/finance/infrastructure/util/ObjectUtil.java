package org.finance.infrastructure.util;

import java.util.function.Function;

/**
 * @author jiangbangfa
 */
public class ObjectUtil {


    public static <V, R> R notNullThen(V value, Function<V, R> then) {
        if (value != null) {
            return then.apply(value);
        }
        return null;
    }

    public static <V> V ifNull(V...vs) {
        for (V v : vs) {
            return v;
        }
        return null;
    }

}

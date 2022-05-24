package org.finance.infrastructure.util;

import java.time.Duration;

/**
 * @author jiangbangfa
 */
public final class CacheKeyUtil {

    public static CacheAttr getUser(String customerNumber, String username) {
        return new CacheAttr(String.format("customerNumber:%s:username:%s", customerNumber, username)
                , Duration.ofHours(2));
    }

    public static CacheAttr getToken(String token) {
        return new CacheAttr(String.format("AuthenticationToken:%s", token), Duration.ofMinutes(60));
    }
}

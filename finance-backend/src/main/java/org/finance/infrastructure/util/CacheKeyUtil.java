package org.finance.infrastructure.util;

import java.time.Duration;

/**
 * @author jiangbangfa
 */
public final class CacheKeyUtil {

    public static CacheAttr getUser(String customerAccount, String username) {
        return new CacheAttr(String.format("customerAccount:%s:username:%s", customerAccount, username)
                , Duration.ofHours(2));
    }

    public static CacheAttr getToken(String token) {
        return new CacheAttr(String.format("AuthenticationToken:%s", token), Duration.ofDays(1));
    }
}

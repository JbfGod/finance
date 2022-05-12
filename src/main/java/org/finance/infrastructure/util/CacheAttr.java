package org.finance.infrastructure.util;

import lombok.Getter;

import java.time.Duration;

/**
 * @author jiangbangfa
 */
@Getter
public class CacheAttr {

    /**
     * 缓存的键值
     */
    private final String key;
    /**
     * 缓存时间(seconds)
     */
    private final Duration timeout;

    public CacheAttr(String key, Duration timeout) {
        this.key = key;
        this.timeout = timeout;
    }
}

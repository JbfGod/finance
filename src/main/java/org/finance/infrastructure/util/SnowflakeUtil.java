package org.finance.infrastructure.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 分布式主键生成工具，雪花算法
 *
 * @author jiangbangfa
 * @date 2022/3/23 上午11:31
 */
@Slf4j
public class SnowflakeUtil {

    private final static long DEFAULT_WORKER_ID = 1;
    private final static long DEFAULT_DATACENTER_ID = 1;
    private final static Snowflake snowflake = IdUtil.getSnowflake(DEFAULT_WORKER_ID, DEFAULT_DATACENTER_ID);

    public static String nextIdStr() {
        return String.valueOf(snowflake.nextId());
    }

    public static Long nextId() {
        return snowflake.nextId();
    }

}

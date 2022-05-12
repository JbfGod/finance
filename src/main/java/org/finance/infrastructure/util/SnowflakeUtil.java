package org.finance.infrastructure.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.finance.infrastructure.exception.HxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 分布式主键生成工具，雪花算法
 *
 * @author jiangbangfa
 */
@Slf4j
@Component
public class SnowflakeUtil {

    private static String localIpAddress;
    private static long WORKER_ID;
    private static long DATACENTER_ID;

    public SnowflakeUtil() {
    }

    public static String getCode() {
        Snowflake snowflake = IdUtil.getSnowflake(WORKER_ID, DATACENTER_ID);
        return String.valueOf(snowflake.nextId());
    }

    private static long generateMaxWorkerId() {
        StringBuilder mpid = new StringBuilder();
        mpid.append(DATACENTER_ID);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            mpid.append(name.split("@")[0]);
        }

        return (long) (mpid.toString().hashCode() & '\uffff') % 32L;
    }

    private static long generateDatacenterId() {
        try {
            InetAddress ip = InetAddress.getByName(localIpAddress);
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            long id;
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (mac == null) {
                    return 1L;
                }
                id = (255L & (long) mac[mac.length - 1] | 65280L & (long) mac[mac.length - 2] << 8) >> 6;
                id %= 32L;
            }

            return id;
        } catch (Exception e) {
            log.error("获取数据中心id失败！", e);
            throw new HxException("获取数据中心id失败:" + e.getMessage());
        }
    }

    @Value("${localIpAddress:127.0.0.1}")
    public void setDatabase(String value) {
        if (value == null) {
            log.warn("未配置'localIpAddress'属性");
        }
        localIpAddress = value;
        DATACENTER_ID = generateDatacenterId();
        WORKER_ID = generateMaxWorkerId();
    }

    public static String getLocalIpAddress() {
        return localIpAddress;
    }

}

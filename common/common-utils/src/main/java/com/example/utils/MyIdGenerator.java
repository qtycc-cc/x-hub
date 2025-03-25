package com.example.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 自定义ID生成器
 */
public class MyIdGenerator {
    /**
     * 生成ID
     * @param workerId 工作机器ID
     * @param dataCenterId 数据中心ID
     * @return ID
     */
    public static Long generateId(Long workerId, Long dataCenterId) {
        Snowflake snowflake = IdUtil.getSnowflake(workerId, dataCenterId);
        return snowflake.nextId();
    }
}

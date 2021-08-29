package com.houkunlin.system.applog.starter.store;

import com.houkunlin.system.applog.starter.AppLogInfo;

/**
 * 应用日志存储对象。
 * 用来存储处理日志信息。
 * 当存在 rabbitmq 环境时会使用  {@link AmqpAppLogStore} 来处理日志信息
 *
 * @author HouKunLin
 */
public interface AppLogStore {
    /**
     * 消费日志方法
     *
     * @param entity 应用操作日志
     */
    void store(AppLogInfo entity);
}

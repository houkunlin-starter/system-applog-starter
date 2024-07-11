package com.houkunlin.system.applog;

/**
 * 应用日志处理器
 *
 * @author HouKunLin
 */
public interface AppLogHandler {
    /**
     * 处理应用日志
     *
     * @param entity 日志信息
     */
    void handle(AppLogInfo entity);

    /**
     * 处理应用日志
     *
     * @param entity    日志信息
     * @param oldObject 数据变更前对象
     * @param newObject 数据变更后对象
     */
    void handle(AppLogInfo entity, final Object oldObject, final Object newObject);
}

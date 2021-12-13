package com.houkunlin.system.applog.starter;

/**
 * 日志记录器，提供一种类似 Slf4j 的形式来记录日志信息
 *
 * @author HouKunLin
 * @since 1.0.4
 */
public interface AppLogger {
    /**
     * 记录日志信息
     *
     * @param msg 常规文本内容
     */
    void log(final String msg);

    /**
     * 记录日志信息
     *
     * @param format   类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray 类似 Slf4J 的日志格式参数信息
     */
    void log(final String format, final Object... argArray);

    /**
     * 记录日志信息
     *
     * @param msg 类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param t   异常信息
     */
    void log(final String msg, final Throwable t);

    /**
     * 记录日志信息
     *
     * @param businessId 业务ID，可通过业务业务类型 loggerName 配合业务ID定位到一条唯一的数据
     * @param msg        常规文本内容
     */
    void log(final String businessId, final String msg);

    /**
     * 记录日志信息
     *
     * @param businessId 业务ID，可通过业务业务类型 loggerName 配合业务ID定位到一条唯一的数据
     * @param format     类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray   类似 Slf4J 的日志格式参数信息
     */
    void log(final String businessId, final String format, final Object... argArray);

    /**
     * 记录日志信息
     *
     * @param businessId 业务ID，可通过业务业务类型 loggerName 配合业务ID定位到一条唯一的数据
     * @param msg        类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param t          异常信息
     */
    void log(final String businessId, final String msg, final Throwable t);

    /**
     * 记录审计日志
     *
     * @param businessId 业务ID，可通过业务业务类型 loggerName 配合业务ID定位到一条唯一的数据
     * @param oldObject  更新前的旧数据
     * @param newObject  更新后的新数据
     * @param format     类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray   类似 Slf4J 的日志格式参数信息
     * @since 1.0.5
     */
    void auditLog(final String businessId, final Object oldObject, final Object newObject, final String format, final Object... argArray);
}

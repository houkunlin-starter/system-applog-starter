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
    void log(String msg);

    /**
     * 记录日志信息
     *
     * @param format   类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray 类似 Slf4J 的日志格式参数信息
     */
    void log(String format, Object... argArray);

    /**
     * 记录日志信息
     *
     * @param msg 类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param t   异常信息
     */
    void log(String msg, Throwable t);
}
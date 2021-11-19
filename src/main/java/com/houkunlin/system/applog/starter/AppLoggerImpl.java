package com.houkunlin.system.applog.starter;

import org.slf4j.Logger;

/**
 * 日志记录器，提供一种类似 Slf4j 的形式来记录日志信息
 *
 * @author HouKunLin
 * @since 1.0.4
 */
public class AppLoggerImpl implements AppLogger {
    private final Logger logger;
    private final String loggerName;

    public AppLoggerImpl(final Logger logger) {
        this.logger = logger;
        this.loggerName = logger.getName();
    }

    @Override
    public void log(String msg) {
        logger.info(msg);
        AppLoggerFactory.logEvent(loggerName, msg);
    }

    @Override
    public void log(String format, Object... argArray) {
        logger.info(format, argArray);
        AppLoggerFactory.logEvent(loggerName, format, argArray);
    }

    @Override
    public void log(String msg, Throwable t) {
        logger.info(msg, t);
        AppLoggerFactory.logEvent(loggerName, msg, t);
    }
}

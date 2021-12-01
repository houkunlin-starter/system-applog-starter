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
        log(null, msg);
    }

    @Override
    public void log(String format, Object... argArray) {
        log(null, format, argArray);
    }

    @Override
    public void log(String msg, Throwable t) {
        log(null, msg, t);
    }

    @Override
    public void log(final String businessId, final String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, msg));
        }
        AppLoggerFactory.logEvent(businessId, loggerName, msg);
    }

    @Override
    public void log(final String businessId, final String format, final Object... argArray) {
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, format), argArray);
        }
        AppLoggerFactory.logEvent(businessId, loggerName, format, argArray);
    }

    @Override
    public void log(final String businessId, final String msg, final Throwable t) {
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, msg), t);
        }
        AppLoggerFactory.logEvent(businessId, loggerName, msg, t);
    }

    private String getMsg(final String businessId, final String msg) {
        if (businessId == null) {
            return msg;
        }
        return String.format("businessId=%s；%s", businessId, msg);
    }
}

package com.houkunlin.system.applog.starter;

import org.slf4j.Logger;

import java.io.Serializable;

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
        logBiz(null, msg);
    }

    @Override
    public void log(String format, Object... argArray) {
        logBiz(null, format, argArray);
    }

    @Override
    public void log(String msg, Throwable t) {
        logBiz(null, msg, t);
    }

    @Override
    public void logBiz(final Serializable businessId, final String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, msg));
        }
        AppLoggerFactory.logEvent(businessId, loggerName, msg);
    }

    @Override
    public void logBiz(final Serializable businessId, final String format, final Object... argArray) {
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, format), argArray);
        }
        AppLoggerFactory.logEvent(businessId, loggerName, format, argArray);
    }

    @Override
    public void logBiz(final Serializable businessId, final String msg, final Throwable t) {
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, msg), t);
        }
        AppLoggerFactory.logEvent(businessId, loggerName, msg, t);
    }

    @Override
    public void auditLog(final Serializable businessId, final Object oldObject, final Object newObject, final String format, final Object... argArray) {
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, format), argArray);
        }
        AppLoggerFactory.auditLogEvent(businessId, oldObject, newObject, loggerName, format, argArray);
    }

    private String getMsg(final Serializable businessId, final String msg) {
        if (businessId == null) {
            return msg;
        }
        return String.format("bizId=%s; %s", businessId, msg);
    }
}

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
        AppLoggerFactory.log(businessId, loggerName, msg);
    }

    @Override
    public void logBiz(final Serializable businessId, final String format, final Object... argArray) {
        Object[] args = handleError(argArray);
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, format), args);
        }
        AppLoggerFactory.log(businessId, loggerName, format, args);
    }

    @Override
    public void logBiz(final Serializable businessId, final String msg, final Throwable t) {
        String message = t.getMessage();
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, msg), message, t);
        }
        AppLoggerFactory.log(businessId, loggerName, msg, message, t);
    }

    @Override
    public void auditLog(final Serializable businessId, final Object oldObject, final Object newObject, final String format, final Object... argArray) {
        Object[] args = handleError(argArray);
        if (logger.isInfoEnabled()) {
            logger.info(getMsg(businessId, format), args);
        }
        AppLoggerFactory.auditLog(businessId, oldObject, newObject, loggerName, format, args);
    }

    private String getMsg(final Serializable businessId, final String msg) {
        if (businessId == null) {
            return msg;
        }
        return String.format("bizId=%s; %s", businessId, msg);
    }

    private Object[] handleError(final Object... argArray) {
        Object[] args = argArray;
        Throwable t = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Throwable throwable) {
                t = throwable;
                args[i] = throwable.getMessage();
            }
        }
        if (t != null) {
            Object[] newArgs = new Object[args.length + 1];
            System.arraycopy(args, 0, newArgs, 0, args.length);
            newArgs[newArgs.length - 1] = t;
            args = newArgs;
        }
        return args;
    }
}

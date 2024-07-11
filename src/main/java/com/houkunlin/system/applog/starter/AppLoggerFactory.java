package com.houkunlin.system.applog.starter;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * 日志记录器，提供一种类似 Slf4j 的形式来记录日志信息
 *
 * @author HouKunLin
 * @since 1.0.4
 */
@Component
@RequiredArgsConstructor
public class AppLoggerFactory implements InitializingBean {
    private static final Map<String, AppLogger> LOGGER_CACHE = new HashMap<>();
    private static final Queue<AppLogInfo> QUEUE = new LinkedList<>();
    private static ICurrentUser CURRENT_USER;
    private static String APPLICATION_NAME;
    private static List<AppLogHandler> HANDLERS;
    /**
     * 日志前缀
     */
    public static final String PREFIX = "biz.";

    private final AppLogProperties appLogProperties;
    private final ICurrentUser currentUser;
    private final List<AppLogHandler> handlers;

    /**
     * 发起日志事件
     *
     * @param businessId 业务ID
     * @param loggerName Slf4J 的日志记录器名称
     * @param format     类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray   类似 Slf4J 的日志格式参数信息
     */
    public static void log(final Serializable businessId, final String loggerName, String format, Object... argArray) {
        final AppLogInfo entity = getAppLogInfo(businessId, loggerName, format, argArray);
        if (HANDLERS == null) {
            QUEUE.add(entity);
            return;
        }

        for (AppLogHandler handler : HANDLERS) {
            handler.handle(entity);
        }
    }

    /**
     * 获取一个应用日志记录器
     *
     * @param name 日志名称（日志类型）
     * @return 应用日志记录器
     */
    public static AppLogger getLogger(String name) {
        return LOGGER_CACHE.computeIfAbsent(name, key -> {
            if (key.startsWith(PREFIX)) {
                return new AppLoggerImpl(LoggerFactory.getLogger(key));
            }
            return new AppLoggerImpl(LoggerFactory.getLogger(PREFIX + key));
        });
    }

    /**
     * 获取一个应用日志记录器
     *
     * @param clazz 日志名称（日志类型）
     * @return 应用日志记录器
     */
    public static AppLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }

    /**
     * 发起日志事件
     *
     * @param businessId 业务ID
     * @param oldObject  更新前的旧数据
     * @param newObject  更新后的新数据
     * @param loggerName Slf4J 的日志记录器名称
     * @param format     类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray   类似 Slf4J 的日志格式参数信息
     * @since 1.0.5
     */
    public static void auditLog(final Serializable businessId, final Object oldObject, final Object newObject, final String loggerName, String format, Object... argArray) {
        final AppLogInfo entity = getAppLogInfo(businessId, loggerName, format, argArray);
        if (HANDLERS == null) {
            QUEUE.add(entity);
            return;
        }

        for (AppLogHandler handler : HANDLERS) {
            handler.handle(entity, oldObject, newObject);
        }
    }

    private static AppLogInfo getAppLogInfo(final Serializable businessId, final String loggerName, String format, Object... argArray) {
        final AppLogInfo entity = new AppLogInfo();
        entity.setDuration(0L);
        entity.setBusinessType(loggerName);
        entity.setBusinessId(businessId);
        entity.setIp(RequestUtil.getRequestIp());
        entity.setApplicationName(APPLICATION_NAME);
        if (CURRENT_USER != null) {
            entity.setCreatedBy(CURRENT_USER.currentUserId());
        }
        if (format == null) {
            return entity;
        }
        entity.setText(format);
        if (argArray == null) {
            return entity;
        }
        if (argArray.length > 0) {
            for (final Object item : argArray) {
                if (item instanceof final Throwable throwable) {
                    entity.setExceptionCode(String.valueOf(throwable.hashCode()));
                }
            }
            entity.setText(MessageFormatter.arrayFormat(format, argArray).getMessage());
        }
        return entity;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AppLoggerFactory.CURRENT_USER = currentUser;
        AppLoggerFactory.HANDLERS = handlers;
        AppLoggerFactory.APPLICATION_NAME = appLogProperties.getApplicationName();
    }

    @PostConstruct
    public void post() {
        if (!QUEUE.isEmpty()) {
            do {
                final AppLogInfo entity = QUEUE.poll();
                if (entity == null) {
                    break;
                }
                for (AppLogHandler handler : HANDLERS) {
                    handler.handle(entity);
                }
            } while (!QUEUE.isEmpty());
        }
    }
}

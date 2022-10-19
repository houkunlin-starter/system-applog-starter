package com.houkunlin.system.applog.starter;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 日志记录器，提供一种类似 Slf4j 的形式来记录日志信息
 *
 * @author HouKunLin
 * @since 1.0.4
 */
@Component
public class AppLoggerFactory {
    private static final Map<String, AppLogger> LOGGER_CACHE = new HashMap<>();
    private static ApplicationEventPublisher applicationEventPublisher;
    private static ICurrentUser currentUser;
    private static String applicationName;
    private static final Queue<ApplicationEvent> QUEUE = new LinkedList<>();
    /**
     * 日志前缀
     */
    public static final String PREFIX = "biz.";

    public AppLoggerFactory(final ApplicationEventPublisher applicationEventPublisher, final AppLogProperties appLogProperties, @Autowired(required = false) final ICurrentUser currentUser) {
        AppLoggerFactory.applicationEventPublisher = applicationEventPublisher;
        AppLoggerFactory.applicationName = appLogProperties.getApplicationName();
        AppLoggerFactory.currentUser = currentUser;
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

    @PostConstruct
    public void post() {
        if (!QUEUE.isEmpty()) {
            do {
                final ApplicationEvent event = QUEUE.poll();
                if (event == null) {
                    break;
                }
                applicationEventPublisher.publishEvent(event);
            } while (!QUEUE.isEmpty());
        }
    }

    /**
     * 发起日志事件
     *
     * @param businessId 业务ID
     * @param loggerName Slf4J 的日志记录器名称
     * @param format     类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray   类似 Slf4J 的日志格式参数信息
     */
    public static void logEvent(final Serializable businessId, final String loggerName, String format, Object... argArray) {
        final AppLogInfo entity = getAppLogInfo(businessId, loggerName);
        final AppLogEvent event = new AppLogEvent(entity, format, argArray);
        if (applicationEventPublisher == null) {
            QUEUE.add(event);
            return;
        }

        applicationEventPublisher.publishEvent(event);
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
    public static void auditLogEvent(final Serializable businessId, final Object oldObject, final Object newObject, final String loggerName, String format, Object... argArray) {
        final AppLogInfo entity = getAppLogInfo(businessId, loggerName);
        final AppLogAuditEvent event = new AppLogAuditEvent(entity, oldObject, newObject, format, argArray);
        if (applicationEventPublisher == null) {
            QUEUE.add(event);
            return;
        }

        applicationEventPublisher.publishEvent(event);
    }

    private static AppLogInfo getAppLogInfo(final Serializable businessId, final String loggerName) {
        final AppLogInfo entity = new AppLogInfo();
        entity.setDuration(0L);
        entity.setBusinessType(loggerName);
        entity.setBusinessId(businessId);
        entity.setIp(RequestUtil.getRequestIp());
        entity.setApplicationName(applicationName);
        if (currentUser != null) {
            entity.setCreatedBy(currentUser.currentUserId());
        }
        return entity;
    }
}

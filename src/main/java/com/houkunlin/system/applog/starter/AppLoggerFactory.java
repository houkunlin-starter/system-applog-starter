package com.houkunlin.system.applog.starter;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private static final Queue<AppLogEvent> QUEUE = new LinkedList<>();
    /**
     * 日志前缀
     */
    public static final String PREFIX = "biz.";

    public AppLoggerFactory(final ApplicationEventPublisher applicationEventPublisher, final AppLogProperties appLogProperties,
                            @Autowired(required = false) final ICurrentUser currentUser) {
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
        return getLogger(clazz.getName());
    }

    @PostConstruct
    public void post() {
        if (!QUEUE.isEmpty()) {
            do {
                final AppLogEvent event = QUEUE.poll();
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
     * @param loggerName Slf4J 的日志记录器名称
     * @param format     类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray   类似 Slf4J 的日志格式参数信息
     */
    public static void logEvent(final String loggerName, String format, Object... argArray) {
        AppLogInfo entity = new AppLogInfo();
        entity.setDuration(0L);
        entity.setType(loggerName);
        entity.setIp(RequestUtil.getRequestIp());
        entity.setApplicationName(applicationName);
        if (currentUser != null) {
            entity.setCreatedBy(currentUser.currentUserId());
        }
        final AppLogEvent event = new AppLogEvent(entity, format, argArray);
        if (applicationEventPublisher == null) {
            QUEUE.add(event);
            return;
        }

        applicationEventPublisher.publishEvent(event);
    }
}

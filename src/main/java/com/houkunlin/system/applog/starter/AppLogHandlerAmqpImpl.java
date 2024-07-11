package com.houkunlin.system.applog.starter;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Async;

/**
 * 把应用日志信息推送到 RabbitMQ 消息队列
 *
 * @author HouKunLin
 */
@RequiredArgsConstructor
public class AppLogHandlerAmqpImpl implements AppLogHandler {
    private final AppLogProperties appLogProperties;
    private final AmqpTemplate amqpTemplate;

    @Async
    @Override
    public void handle(AppLogInfo entity) {
        amqpTemplate.convertAndSend(appLogProperties.getMqExchange(),
                appLogProperties.getMqRoutingKey(),
                entity);
    }

    @Override
    public void handle(AppLogInfo entity, Object oldObject, Object newObject) {
        amqpTemplate.convertAndSend(appLogProperties.getMqExchange(),
                appLogProperties.getMqRoutingKey(),
                entity);
    }
}

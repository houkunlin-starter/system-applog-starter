package com.houkunlin.system.applog.starter.store;

import com.houkunlin.system.applog.starter.AppLogInfo;
import com.houkunlin.system.applog.starter.AppLogProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;

import javax.annotation.PostConstruct;

/**
 * 使用 Rabbitmq 来处理日志信息，该对象会把日志信息发送到 Rabbitmq ，然后由特殊的应用从 {@link AppLogProperties#getMqQueue()} 队列获取日志信息
 *
 * @author HouKunLin
 */
public class AmqpAppLogStore implements AppLogStore {
    private static final Logger logger = LoggerFactory.getLogger(AmqpAppLogStore.class);
    private final AmqpTemplate amqpTemplate;
    /**
     * App 日志交换器名称
     */
    private final String mqExchange;
    /**
     * App 日志消息队列路由键名称
     */
    private final String mqRoutingKey;

    public AmqpAppLogStore(final AmqpTemplate amqpTemplate, final AppLogProperties appLogProperties) {
        this.amqpTemplate = amqpTemplate;
        this.mqExchange = appLogProperties.getMqExchange();
        this.mqRoutingKey = appLogProperties.getMqRoutingKey();
    }

    @Override
    public void store(final AppLogInfo entity) {
        amqpTemplate.convertAndSend(mqExchange,
                mqRoutingKey,
                entity);
    }

    @PostConstruct
    public void post() {
        logger.info("使用 AmqpTemplate 来通知其他系统存储日志信息");
    }
}

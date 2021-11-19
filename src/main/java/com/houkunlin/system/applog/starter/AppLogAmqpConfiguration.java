package com.houkunlin.system.applog.starter;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * 应用操作日志配置
 *
 * @author HouKunLin
 */
@ConditionalOnProperty(prefix = "system.applog", name = "mq-amqp-enabled")
@ConditionalOnClass(AmqpTemplate.class)
@Configuration
@AllArgsConstructor
public class AppLogAmqpConfiguration {
    private final AppLogProperties appLogProperties;
    private final AmqpTemplate amqpTemplate;

    /**
     * 配置日志队列
     *
     * @return 队列
     */
    @Bean
    public Queue appLogQueue() {
        return new Queue(appLogProperties.getMqQueue());
    }

    /**
     * 配置日志交换器
     *
     * @return 日志交换器
     */
    @Bean
    public TopicExchange appLogExchange() {
        return new TopicExchange(appLogProperties.getMqExchange());
    }

    /**
     * 配置日志交换器和队列的绑定，已经绑定路由key
     *
     * @param appLogQueue    日志队列
     * @param appLogExchange 日志交换器
     * @return 绑定关系
     */
    @Bean
    public Binding appLogBindingExchangeMessage(Queue appLogQueue, TopicExchange appLogExchange) {
        return BindingBuilder.bind(appLogQueue).to(appLogExchange).with(appLogProperties.getMqRoutingKey());
    }

    @Async
    @EventListener
    public void eventListener(final AppLogEvent event) {
        amqpTemplate.convertAndSend(appLogProperties.getMqExchange(),
                appLogProperties.getMqRoutingKey(),
                event.getSource());
    }
}

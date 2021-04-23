package com.houkunlin.system.applog.starter.mq;

import com.houkunlin.system.applog.starter.AppLogProperties;
import com.houkunlin.system.applog.starter.store.AppLogStore;
import com.houkunlin.system.applog.starter.store.RabbitMqAppLogStore;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 应用操作日志配置
 *
 * @author HouKunLin
 */
@ConditionalOnClass(AmqpTemplate.class)
@Configuration
public class AppLogMqConfiguration {
    private final AppLogProperties appLogProperties;

    public AppLogMqConfiguration(final AppLogProperties appLogProperties) {
        this.appLogProperties = appLogProperties;
    }

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
    TopicExchange appLogExchange() {
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

    @ConditionalOnMissingBean
    @Bean
    public AppLogStore appLogStore(final AmqpTemplate amqpTemplate, final AppLogProperties appLogProperties) {
        return new RabbitMqAppLogStore(amqpTemplate, appLogProperties);
    }
}

package com.houkunlin.system.applog.starter;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用日志配置
 *
 * @author HouKunLin
 */
@Data
@ToString
@Configuration
@ConfigurationProperties("system.applog")
public class AppLogProperties {
    /**
     * App 日志交换器名称
     */
    private String mqExchange = "app.topic";
    /**
     * App 日志消息队列队列名称
     */
    private String mqQueue = "log";
    /**
     * App 日志消息队列路由键名称
     */
    private String mqRoutingKey = "route.AppLog";
    /**
     * 日志来源应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName = "${spring.application.name}";
}

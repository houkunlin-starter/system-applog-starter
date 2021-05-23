package tests.application.server.local;

import com.houkunlin.system.applog.starter.AppLogInfo;
import com.houkunlin.system.applog.starter.store.AppLogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HouKunLin
 */
@ConditionalOnClass(AmqpTemplate.class)
@Configuration
public class StoreConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(StoreConfiguration.class);

    /**
     * 作为日志服务，需要处理来自 rabbitmq 的应用日志信息，然后把日志保存到数据库系统中
     *
     * @param info 应用日志
     */
    @RabbitListener(queues = "#{appLogProperties.mqQueue}")
    public void readLog(AppLogInfo info) {
        logger.info("通过 MQ 获取到日志信息：{}", info);
    }

    /**
     * 直接提供一个 AppLogStore Bean 对象，使得本系统的应用日志不走 MQ 渠道，而是直接内部处理
     *
     * @return AppLogStore
     */
    @Bean
    public AppLogStore appLogStore() {
        return new AppLogStore() {
            @Override
            public void store(final AppLogInfo entity) {
                logger.info("本地直接处理应用日志： {}", entity);
            }
        };
    }
}
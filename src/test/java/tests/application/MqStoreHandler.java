package tests.application;

import com.houkunlin.system.applog.starter.AppLogInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @author HouKunLin
 */
@ConditionalOnClass(AmqpTemplate.class)
@Configuration
public class MqStoreHandler {
    private static final Logger logger = LoggerFactory.getLogger(MqStoreHandler.class);

    @RabbitListener(queues = "#{appLogProperties.mqQueue}")
    public void readLog(AppLogInfo info) {
        logger.info("通过 MQ 获取到日志信息：{}", info);
    }
}

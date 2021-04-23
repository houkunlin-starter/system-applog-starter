package com.houkunlin.system.applog.starter;

import com.houkunlin.system.applog.starter.store.AppLogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 自动配置
 *
 * @author HouKunLin
 */
@ComponentScan
public class SystemAppLogStarter {
    private static final Logger logger = LoggerFactory.getLogger(SystemAppLogStarter.class);

    /**
     * 提供一个默认的日志存储对象，避免系统启动失败
     *
     * @return AppLogStore
     */
    @ConditionalOnMissingBean
    @Bean
    public AppLogStore appLogStore() {
        logger.debug("未找到 AppLogStore 处理日志信息，提供一个空的 AppLogStore 对象");
        return new AppLogStore() {
            @Override
            public void store(final AppLogInfo entity) {
                logger.debug("处理日志： {}", entity);
            }
        };
    }
}

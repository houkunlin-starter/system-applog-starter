package com.houkunlin.system.applog.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;

/**
 * 自动配置
 *
 * @author HouKunLin
 */
@Slf4j
@ComponentScan
@Configuration(proxyBeanMethods = false)
@Import(SystemAppLogUserConfiguration.class)
public class SystemAppLogStarter {
    /**
     * 提供一个 SpEL 的解析上下文对象
     *
     * @return ParserContext
     */
    @ConditionalOnMissingBean
    @Bean
    public ParserContext parserContext() {
        return new TemplateParserContext();
    }

    /**
     * 提供一个默认的对象
     *
     * @return 当前用户
     */
    @ConditionalOnMissingBean
    @Bean
    public ICurrentUser currentUser() {
        log.warn("没有提供 ICurrentUser 对象，默认提供一个返回 null 的 ICurrentUser 对象");
        return () -> null;
    }
}

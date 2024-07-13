package com.houkunlin.system.applog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 提供一个默认的应用日志字符串模板处理器
     *
     * @param parserContext SpEL 的解析上下文对象
     * @return 模板处理器
     */
    @ConditionalOnMissingBean
    @Bean
    public TemplateParserDefaultImpl appLogTemplateParser(@Autowired(required = false) ParserContext parserContext) {
        return new TemplateParserDefaultImpl(parserContext == null ? new TemplateParserContext() : parserContext);
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

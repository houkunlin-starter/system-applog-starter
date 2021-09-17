package com.houkunlin.system.applog.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;

/**
 * 自动配置
 *
 * @author HouKunLin
 */
@ComponentScan
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
}

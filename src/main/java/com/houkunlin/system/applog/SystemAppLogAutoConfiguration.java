package com.houkunlin.system.applog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;

import java.util.List;

/**
 * 自动配置
 *
 * @author HouKunLin
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@Import({SystemAppLogUserConfiguration.class, AppLogProperties.class})
@RequiredArgsConstructor
public class SystemAppLogAutoConfiguration {

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

    @Bean
    public AppLoggerFactory appLoggerFactory(AppLogProperties appLogProperties,
                                             List<AppLogHandler> handlers,
                                             ICurrentUser currentUser) {
        return new AppLoggerFactory(appLogProperties, currentUser, handlers);
    }

    @ConditionalOnClass(JoinPoint.class)
    @Configuration(proxyBeanMethods = false)
    static public class AppLogAspectAutoConfiguration {
        @Bean
        public AppLogAspect appLogAspect(TemplateParser templateParser,
                                         AppLogProperties appLogProperties,
                                         List<AppLogHandler> handlers,
                                         ICurrentUser currentUser) {
            return new AppLogAspect(templateParser, handlers, currentUser, appLogProperties.getApplicationName());
        }
    }
}

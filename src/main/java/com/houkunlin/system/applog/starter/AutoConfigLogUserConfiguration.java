package com.houkunlin.system.applog.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 自动配置日志用户ID信息
 *
 * @author HouKunLin
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SecurityContextHolder.class)
@ConditionalOnMissingBean(ICurrentUser.class)
public class AutoConfigLogUserConfiguration {

    @Bean
    public ICurrentUser currentUser() {
        return () -> {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return null;
            }
            return authentication.getName();
        };
    }
}

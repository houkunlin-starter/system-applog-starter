package com.houkunlin.system.applog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 操作日志、应用日志AOP配置
 *
 * @author HouKunLin
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AppLogAspect implements InitializingBean {
    private final AppLogTemplateParser appLogTemplateParser;
    private final AppLogProperties appLogProperties;
    private final List<AppLogHandler> handlers;
    /**
     * 获取当前登录用户ID
     */
    private final ICurrentUser currentUser;
    private String applicationName = "";

    @SuppressWarnings({"unchecked"})
    @Around("@annotation(annotation)")
    public Object around(ProceedingJoinPoint pjp, AppLog annotation) throws Throwable {
        Object result = null;
        Exception exception = null;
        long start = System.nanoTime();
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            exception = e;
        }
        long end = System.nanoTime();

        AppLogInfo entity = new AppLogInfo();
        entity.setDuration((end - start) / 1000000);
        entity.setIp(RequestUtil.getRequestIp());
        entity.setApplicationName(applicationName);

        Object context = appLogTemplateParser.createContext(pjp, result, exception);
        final String createdBy = appLogTemplateParser.parseExpression(annotation.createdBy(), context);
        if (StringUtils.hasText(createdBy)) {
            entity.setCreatedBy(createdBy);
        } else {
            entity.setCreatedBy(currentUser.currentUserId());
        }

        entity.setBusinessType(appLogTemplateParser.parseExpression(annotation.businessType(), context));
        entity.setBusinessId(appLogTemplateParser.parseExpression(annotation.businessId(), context));

        if (exception == null) {
            entity.setText(appLogTemplateParser.parseExpression(annotation.value(), context));
        } else {
            entity.setExceptionCode(String.valueOf(exception.hashCode()));
            String messageTpl = annotation.errorValue();
            if (!StringUtils.hasText(messageTpl)) {
                messageTpl = annotation.value() + "；发生了错误：#{e.message}";
            }
            entity.setText(appLogTemplateParser.parseExpression(messageTpl, context));
        }
        handlers.forEach(handler -> {
            try {
                handler.handle(entity);
            } catch (Exception e) {
                log.error("处理应用日志时发生异常，异常处理器：{}", handler.getClass().getName(), e);
            }
        });

        if (exception != null) {
            throw exception;
        }

        return result;
    }

    @Override
    public void afterPropertiesSet() {
        this.applicationName = appLogProperties.getApplicationName();
    }
}

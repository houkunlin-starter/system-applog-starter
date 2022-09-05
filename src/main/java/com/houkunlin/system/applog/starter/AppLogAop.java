package com.houkunlin.system.applog.starter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 操作日志AOP配置
 *
 * @author HouKunLin
 * @date 2020/8/11 0011 16:25
 */
@Aspect
@Component
public class AppLogAop implements BeanFactoryAware, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(AppLogAop.class);
    private final ExpressionParser parser = new SpelExpressionParser();
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    private final ParserContext parserContext;
    private final String applicationName;
    private final ApplicationEventPublisher applicationEventPublisher;
    private BeanResolver beanResolver;
    /**
     * 模板字符串需要的最小长度
     */
    private final int spelStrMinLen;
    /**
     * 获取当前登录用户ID
     */
    private final ICurrentUser currentUser;

    public AppLogAop(final ParserContext parserContext, final AppLogProperties appLogProperties, final ApplicationEventPublisher applicationEventPublisher,
                     @Autowired(required = false) ICurrentUser currentUser) {
        this.parserContext = parserContext;
        this.applicationName = appLogProperties.getApplicationName();
        // 模板字符串最少需要一个前后缀，再加一个变量信息长度，变量信息至少两个字符（#a），不存在只有一个字符的顶级变量
        // 例如：最小长度为5，是因为一个 SpEL 表达式最少需要 #{#a} 个字符
        this.spelStrMinLen = (parserContext.getExpressionPrefix() + parserContext.getExpressionSuffix()).length() + 2;
        this.applicationEventPublisher = applicationEventPublisher;
        this.currentUser = currentUser;
    }

    @Pointcut("@annotation(AppLog)")
    public void aop() {
    }

    @Around("aop()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = getMethod(pjp);
        Object result = null;
        Exception exception = null;
        long start = System.nanoTime();
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            exception = e;
        }

        AppLog annotation = method.getAnnotation(AppLog.class);
        if (annotation != null) {
            AppLogInfo entity = new AppLogInfo();
            entity.setDuration((System.nanoTime() - start) / 1000000);
            entity.setIp(RequestUtil.getRequestIp());
            entity.setApplicationName(applicationName);

            RootObject rootObject = getRootObject(pjp, method, result, exception);
            EvaluationContext context = createEvaluationContext(rootObject, method, pjp.getArgs());
            final String createdBy = parseExpression(annotation.createdBy(), context);
            if (StringUtils.hasText(createdBy)) {
                entity.setCreatedBy(createdBy);
            } else if (currentUser != null) {
                entity.setCreatedBy(currentUser.currentUserId());
            }

            entity.setBusinessType(parseExpression(annotation.businessType(), context));
            entity.setBusinessId(parseExpression(annotation.businessId(), context));

            if (exception == null) {
                entity.setText(parseExpression(annotation.value(), context));
            } else {
                entity.setExceptionCode(String.valueOf(exception.hashCode()));
                String messageTpl = annotation.errorValue();
                if (!StringUtils.hasText(messageTpl)) {
                    messageTpl = annotation.value() + "；发生了错误：#{e.message}";
                }
                entity.setText(parseExpression(messageTpl, context));
            }
            consumerAppLog(entity);
        }

        if (exception != null) {
            throw exception;
        }

        return result;
    }

    private void consumerAppLog(AppLogInfo entity) {
        applicationEventPublisher.publishEvent(new AppLogEvent(entity));
    }

    private RootObject getRootObject(ProceedingJoinPoint pjp, Method method, Object result, Exception e) {
        return new RootObject(method, pjp.getArgs(), pjp.getTarget(), pjp.getTarget().getClass(), result, e);
    }

    /**
     * 解析SPEL
     *
     * @param message SpEL表达式
     * @return 解析结果
     */
    private String parseExpression(String message, EvaluationContext context) {
        if (message.length() < spelStrMinLen || !message.contains(parserContext.getExpressionPrefix())) {
            return message;
        }

        try {
            return parser.parseExpression(message, parserContext).getValue(context, String.class);
        } catch (EvaluationException | ParseException e) {
            if (logger.isErrorEnabled()) {
                logger.error("应用日志 SpEL 解析错误：" + message, e);
            }
            return message;
        }
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }

    /**
     * 构建上下文参数
     *
     * @param rootObj 根对象
     * @param method  方法对象
     * @param args    方法参数值列表
     * @return 上下文
     */
    private EvaluationContext createEvaluationContext(Object rootObj, Method method, Object[] args) {
        final StandardEvaluationContext context = new StandardEvaluationContext(rootObj);
        context.setBeanResolver(beanResolver);

        // 参照 org.springframework.context.expression.MethodBasedEvaluationContext.lazyLoadArguments
        if (!ObjectUtils.isEmpty(args)) {
            String[] paramNames = discoverer.getParameterNames(method);
            int paramCount = (paramNames != null ? paramNames.length : method.getParameterCount());
            int argsCount = args.length;

            for (int i = 0; i < paramCount; i++) {
                Object value = null;
                if (argsCount > paramCount && i == paramCount - 1) {
                    // 将剩余参数公开为最后一个参数的vararg数组
                    value = Arrays.copyOfRange(args, i, argsCount);
                } else if (argsCount > i) {
                    // 找到实际参数-否则保留为null
                    value = args[i];
                }
                context.setVariable("a" + i, value);
                context.setVariable("p" + i, value);
                if (paramNames != null && paramNames[i] != null) {
                    context.setVariable(paramNames[i], value);
                }
            }
        }
        try {
            context.registerFunction("hasText", StringUtils.class.getMethod("hasText", CharSequence.class));
            context.registerFunction("trimWhitespace", StringUtils.class.getMethod("trimWhitespace", String.class));
        } catch (NoSuchMethodException ignored) {
        }
        return context;
    }

    @Override
    public void setBeanFactory(@NonNull final BeanFactory beanFactory) throws BeansException {
        this.beanResolver = new BeanFactoryResolver(beanFactory);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert beanResolver != null;
    }
}

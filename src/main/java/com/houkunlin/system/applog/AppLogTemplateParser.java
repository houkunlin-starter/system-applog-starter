package com.houkunlin.system.applog;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 应用日志表达式（模板）解析器
 *
 * @author HouKunLin
 */
public interface AppLogTemplateParser<CONTEXT> {
    /**
     * 创建表达式（模板）上下文对象；构建变量内容
     *
     * @param pjp       AOP切点对象
     * @param result    实际执行方法的返回值
     * @param exception 实际执行方法的抛出异常对象
     * @return 表达式（模板）上下文对象
     */
    CONTEXT createContext(ProceedingJoinPoint pjp, Object result, Exception exception);

    /**
     * 解析表达式（模板）内容
     *
     * @param expression 表达式（模板）内容
     * @param context    上下文对象
     * @return 解析结果
     */
    String parseExpression(String expression, CONTEXT context);
}

package com.houkunlin.system.applog.starter;

import java.lang.annotation.*;

/**
 * 记录操作日志、应用日志。
 * <p>用法： 在Controller方法接口上增加 @AppLog("接口操作说明") @AppLog(value = "接口操作说明", type = "系统日志") 注解</p>
 * <p>用法： 支持 SpEL 表达式
 * <ul>
 *     <li>@AppLog("接口操作说明，方法名称：#{method.name}")</li>
 *     <li>@AppLog("接口操作说明，方法参数：#{args[0].toString()}")</li>
 *     <li>@AppLog("接口操作说明，方法参数：#{#id}") 其中的 id 为方法参数名称</li>
 *     <li>@AppLog("接口操作说明，当前对象：#{target.toString()}")</li>
 *     <li>@AppLog("接口操作说明，当前对象class：#{targetClass.getName()}")</li>
 *     <li>@AppLog("接口操作说明，返回结果：#{result?.toString()?:'无返回值'}")</li>
 * </ul>
 *
 * </p>
 *
 * @author HouKunLin
 * @date 2020/8/11 0011 16:23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppLog {
    /**
     * 日志内容；支持 SpEL 表达式
     *
     * @return 日志内容。支持 SpEL 表达式
     */
    String value() default "";

    /**
     * 发生错误时的日志内容，默认为与 value 内容一致，但在后面加了 exception.getMessage() 的内容；支持 SpEL 表达式
     *
     * @return 发生错误时的日志内容。支持 SpEL 表达式
     */
    String errorValue() default "";

    /**
     * 操作类型、业务类型（自行传入，建议使用对象名称，或者对象完整包名）；
     * 支持 SpEL 表达式
     *
     * @return 操作类型。
     */
    String businessType() default "";

    /**
     * 操作记录关联的业务ID，通过 {@link #businessType} + businessId 一般情况下可定位到一条相对应的数据信息；
     * 支持 SpEL 表达式
     *
     * @return 操作类型。
     */
    String businessId() default "";

    /**
     * 日志创建人；支持 SpEL 表达式
     *
     * @return 用户唯一主键。支持 SpEL 表达式
     */
    String createdBy() default "";
}

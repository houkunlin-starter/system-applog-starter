package com.houkunlin.system.applog;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志记录信息对象
 *
 * @author HouKunLin
 */
@Data
@ToString
@EqualsAndHashCode
public class AppLogInfo implements Serializable {
    /**
     * 创建时间
     */
    private final LocalDateTime createdAt = LocalDateTime.now();
    /**
     * 发生异常时的错误代码（异常的 hashCode 值）
     */
    private String exceptionCode;
    /**
     * 操作类型、业务类型（自行传入，建议使用对象名称，或者对象完整包名）
     */
    private String businessType;
    /**
     * 操作记录关联的业务ID，通过 {@link #businessType} + businessId 一般情况下可定位到一条相对应的数据信息
     */
    private Serializable businessId;
    /**
     * 操作内容
     */
    private String text;
    /**
     * 操作IP
     */
    private String ip;
    /**
     * 耗时、时长（单位：毫秒）
     */
    private Long duration;
    /**
     * 操作用户ID
     */
    private Serializable createdBy;
    /**
     * 应用名称。记录本条日志来自那个应用服务
     */
    private String applicationName;
}

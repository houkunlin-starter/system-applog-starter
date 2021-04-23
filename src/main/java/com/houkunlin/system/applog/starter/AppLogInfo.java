package com.houkunlin.system.applog.starter;

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
     * 发生异常时的错误代码
     */
    private String exceptionCode;
    /**
     * 操作类型（暂未定，自行传入）
     */
    private String type;
    /**
     * 操作内容
     */
    private String text;
    /**
     * 操作IP
     */
    private String ip;
    /**
     * 耗时、时长
     */
    private Long duration;
    /**
     * 操作用户ID
     */
    private String createdBy;
}

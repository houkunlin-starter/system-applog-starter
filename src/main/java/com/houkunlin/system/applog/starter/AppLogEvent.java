package com.houkunlin.system.applog.starter;

import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.ApplicationEvent;

/**
 * 应用日志事件
 *
 * @author HouKunLin
 * @since 1.0.3
 */
@Getter
public class AppLogEvent extends ApplicationEvent {
    /**
     * 类似 Slf4J 的日志格式（支持 {} 占位符）
     *
     * @since 1.0.4
     */
    protected final String format;
    /**
     * 类似 Slf4J 的日志格式参数信息
     *
     * @since 1.0.4
     */
    protected final transient Object[] argArray;

    protected boolean isNotFirst = false;

    public AppLogEvent(final AppLogInfo source) {
        super(source);
        this.format = null;
        this.argArray = null;
    }

    /**
     * 构造器
     *
     * @param source   日志对象
     * @param format   类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray 类似 Slf4J 的日志格式参数信息
     * @since 1.0.4
     */
    public AppLogEvent(final AppLogInfo source, String format, Object... argArray) {
        super(source);
        this.format = format;
        this.argArray = argArray;
    }


    @Override
    public AppLogInfo getSource() {
        final AppLogInfo logInfo = (AppLogInfo) super.getSource();
        if (format == null) {
            return logInfo;
        }
        if (argArray == null) {
            return logInfo;
        }
        if (isNotFirst) {
            return logInfo;
        }
        isNotFirst = true;
        if (argArray.length > 0) {
            for (int i = 0; i < argArray.length; i++) {
                final Object item = argArray[i];
                if (item instanceof Throwable) {
                    final Throwable throwable = (Throwable) item;
                    argArray[i] = throwable.getMessage();
                    logInfo.setExceptionCode(String.valueOf(throwable.hashCode()));
                }
            }
        }
        logInfo.setText(MessageFormatter.arrayFormat(format, argArray).getMessage());
        return logInfo;
    }
}

package com.houkunlin.system.applog.starter;

import org.springframework.context.ApplicationEvent;

/**
 * 应用日志事件
 *
 * @author HouKunLin
 * @see 1.0.3
 */
public class AppLogEvent extends ApplicationEvent {
    public AppLogEvent(final AppLogInfo source) {
        super(source);
    }

    @Override
    public AppLogInfo getSource() {
        return (AppLogInfo) super.getSource();
    }
}

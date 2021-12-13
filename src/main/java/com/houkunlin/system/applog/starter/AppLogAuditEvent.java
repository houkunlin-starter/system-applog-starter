package com.houkunlin.system.applog.starter;

import lombok.Getter;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;

/**
 * 应用日志审计事件
 *
 * @author HouKunLin
 * @since 1.0.4
 */
@Getter
public class AppLogAuditEvent extends AppLogEvent {
    public static final Javers JAVERS = JaversBuilder.javers().withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE).build();
    protected final transient Object oldObject;
    protected final transient Object newObject;

    /**
     * 构造器
     *
     * @param source   日志对象
     * @param format   类似 Slf4J 的日志格式（支持 {} 占位符）
     * @param argArray 类似 Slf4J 的日志格式参数信息
     * @since 1.0.4
     */
    public AppLogAuditEvent(final AppLogInfo source, final Object oldObject, final Object newObject, String format, Object... argArray) {
        super(source, format, argArray);
        this.oldObject = oldObject;
        this.newObject = newObject;
    }

    public Diff getDiff() {
        return JAVERS.compare(oldObject, newObject);
    }
}

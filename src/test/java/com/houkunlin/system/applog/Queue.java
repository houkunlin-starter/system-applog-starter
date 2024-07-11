package com.houkunlin.system.applog;

import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class Queue implements AppLogHandler {
    public static final java.util.Queue<AppLogInfo> QUEUE = new LinkedList<>();

    @Override
    public void handle(AppLogInfo entity) {
        QUEUE.add(entity);
    }

    @Override
    public void handle(AppLogInfo entity, Object oldObject, Object newObject) {
        QUEUE.add(entity);
    }
}

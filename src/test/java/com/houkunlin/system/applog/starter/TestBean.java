package com.houkunlin.system.applog.starter;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author HouKunLin
 */
@Component
public class TestBean {
    public String now() {
        return "2024-01-01 00:00:00";
    }
}

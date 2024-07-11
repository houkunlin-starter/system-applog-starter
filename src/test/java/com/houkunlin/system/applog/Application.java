package com.houkunlin.system.applog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 日志服务。处理保存来自其他系统的日志信息
 *
 * @author HouKunLin
 */
@Slf4j
@RestControllerAdvice
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @ResponseStatus
    @ExceptionHandler(value = Exception.class)
    public Object exception(Exception e) {
        log.error("Exception {}", e.getMessage());
        return e.getMessage();
    }
}

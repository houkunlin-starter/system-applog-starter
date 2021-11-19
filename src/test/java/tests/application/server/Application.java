package tests.application.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tests.application.common.CommandRunnerTests;

/**
 * 日志服务。处理保存来自其他系统的日志信息
 *
 * @author HouKunLin
 */
@SpringBootApplication(scanBasePackageClasses = {CommandRunnerTests.class, Application.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}

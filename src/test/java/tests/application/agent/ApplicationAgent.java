package tests.application.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tests.application.common.CommandRunnerTests;

/**
 * 日志客户端。只提供日志信息，但是并不保存日志信息。需要依赖 rabbitmq 服务通知相应的系统来保存日志信息
 *
 * @author HouKunLin
 */
@SpringBootApplication(scanBasePackageClasses = {CommandRunnerTests.class, ApplicationAgent.class})
public class ApplicationAgent {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationAgent.class);
    }
}

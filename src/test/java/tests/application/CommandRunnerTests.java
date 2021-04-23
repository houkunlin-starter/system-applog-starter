package tests.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动完成后执行一段代码
 *
 * @author HouKunLin
 */
@Component
public class CommandRunnerTests implements CommandLineRunner {
    private final TestAppLogController controller;

    public CommandRunnerTests(final TestAppLogController controller) {
        this.controller = controller;
    }

    @Override
    public void run(final String... args) {
        try {
            controller.testLog1();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            controller.testLog2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            controller.testLog3();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

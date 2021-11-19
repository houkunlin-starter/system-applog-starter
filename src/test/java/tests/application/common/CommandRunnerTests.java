package tests.application.common;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动完成后执行一段代码
 *
 * @author HouKunLin
 */
@Component
@RequiredArgsConstructor
public class CommandRunnerTests implements CommandLineRunner {
    private final TestAppLogController controller;

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

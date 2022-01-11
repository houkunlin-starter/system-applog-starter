package tests.application.common;

import com.houkunlin.system.applog.starter.AppLog;
import com.houkunlin.system.applog.starter.AppLogger;
import com.houkunlin.system.applog.starter.AppLoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HouKunLin
 */
@RestController
public class TestAppLogController {
    public static final AppLogger APP_LOGGER = AppLoggerFactory.getLogger("test");

    @AppLog("测试成功日志 - #{result} , 在什么时候：#{@testBean.now()}")
    @GetMapping("/testLog1")
    public Object testLog1() {
        APP_LOGGER.log("testLog1 记录普通的日志信息");
        APP_LOGGER.logBiz("testLog1 记录普通的日志信息，并且记录参数信息 {}", "这是参数信息");
        return "返回信息";
    }

    @AppLog(value = "测试日志1")
    @GetMapping("/testLog2")
    public Object testLog2() {
        final RuntimeException exception = new RuntimeException("测试异常信息");
        APP_LOGGER.log("testLog2 记录普通的日志信息");
        APP_LOGGER.logBiz("testLog2 记录普通的日志信息，并且记录参数信息 {}", "这是参数信息");
        APP_LOGGER.logBiz("testLog2 记录普通的日志信息，并且记录参数信息 {} 和异常信息 {}", "这是参数信息", exception);
        APP_LOGGER.log("testLog2 记录普通的日志信息，并且记录参数信息，异常信息 {}", exception);
        throw exception;
    }

    @AppLog(value = "测试日志2", errorValue = "测试发生了错误，错误信息：#{e.message}")
    @GetMapping("/testLog3")
    public Object testLog3() {
        final RuntimeException exception = new RuntimeException("测试异常信息");
        APP_LOGGER.log("testLog3 记录普通的日志信息");
        APP_LOGGER.logBiz("testLog3 记录普通的日志信息，并且记录参数信息 {}", "这是参数信息");
        APP_LOGGER.logBiz("testLog3 第一个参数 {}， 第二个参数是异常 {}，第三个参数 {}，第四个参数是异常 {}", "这是参数信息", exception, "其他信息", exception);
        APP_LOGGER.log("testLog3 记录普通的日志信息，并且记录参数信息，异常信息 {}", exception);
        throw exception;
    }
}

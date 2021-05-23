package tests.application.common;

import com.houkunlin.system.applog.starter.AppLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HouKunLin
 */
@RestController
public class TestAppLogController {
    @AppLog("测试成功日志 - #{result} , 在什么时候：#{@testBean.now()}")
    @GetMapping("/testLog1")
    public Object testLog1() {
        return "返回信息";
    }

    @AppLog(value = "测试日志1")
    @GetMapping("/testLog2")
    public Object testLog2() {
        throw new RuntimeException("测试异常信息");
    }

    @AppLog(value = "测试日志2", errorValue = "测试发生了错误，错误信息：#{e.message}")
    @GetMapping("/testLog3")
    public Object testLog3() {
        throw new RuntimeException("测试异常信息");
    }
}

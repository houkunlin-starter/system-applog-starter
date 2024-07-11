package com.houkunlin.system.applog.starter;

import com.houkunlin.system.applog.starter.bean.Form;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HouKunLin
 */
@RestController
public class TestAppLogController {

    @AppLog("测试成功日志 - #{result} , 在什么时候：#{@testBean.now()}")
    @GetMapping("/m11")
    public Object m11() {
        return "返回信息";
    }

    @AppLog(value = "测试成功日志 - #{result} , 在什么时候：#{@testBean.now()}", businessId = "1")
    @GetMapping("/m12")
    public Object m12() {
        return "返回信息";
    }

    @AppLog(value = "测试成功日志 - #{result} , 在什么时候：#{@testBean.now()}", businessId = "1", businessType = "type")
    @GetMapping("/m13")
    public Object m13() {
        return "返回信息";
    }

    @AppLog("测试成功日志 - #{result} , 在什么时候：#{@testBean.now()}")
    @GetMapping("/m14")
    public Object m14() {
        throw new RuntimeException("测试异常信息");
    }

    @AppLog(value = "测试日志1")
    @GetMapping("/m21")
    public Object m21() {
        throw new RuntimeException("测试异常信息");
    }

    @AppLog(value = "测试日志2", errorValue = "测试发生了错误，错误信息：#{e.message}")
    @GetMapping("/m22")
    public Object m22() {
        throw new RuntimeException("测试异常信息");
    }

    @AppLog(value = "#{#form.id != null ? '修改' : '新增'}数据")
    @PostMapping("/m31")
    public Object m31(@RequestBody Form form) {
        return form;
    }
}


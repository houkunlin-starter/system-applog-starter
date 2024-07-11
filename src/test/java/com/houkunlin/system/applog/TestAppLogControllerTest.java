package com.houkunlin.system.applog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TestAppLogControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void m11() throws Exception {
        mvc.perform(get("/m11"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().string("返回信息"));

        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("测试成功日志 - 返回信息 , 在什么时候：2024-01-01 00:00:00", logInfo.getText());
    }

    @Test
    void m12() throws Exception {
        mvc.perform(get("/m12"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().string("返回信息"));

        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("测试成功日志 - 返回信息 , 在什么时候：2024-01-01 00:00:00", logInfo.getText());
        assertEquals("1", logInfo.getBusinessId());
    }

    @Test
    void m13() throws Exception {
        mvc.perform(get("/m13"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().string("返回信息"));

        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("测试成功日志 - 返回信息 , 在什么时候：2024-01-01 00:00:00", logInfo.getText());
        assertEquals("1", logInfo.getBusinessId());
        assertEquals("type", logInfo.getBusinessType());
    }

    @Test
    void m14() throws Exception {
        mvc.perform(get("/m14"))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("测试异常信息"));

        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("测试成功日志 -  , 在什么时候：2024-01-01 00:00:00；发生了错误：测试异常信息", logInfo.getText());
    }

    @Test
    void m21() throws Exception {
        mvc.perform(get("/m21"))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("测试异常信息"));

        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("测试日志1；发生了错误：测试异常信息", logInfo.getText());
    }

    @Test
    void m22() throws Exception {
        mvc.perform(get("/m22"))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("测试异常信息"));

        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("测试发生了错误，错误信息：测试异常信息", logInfo.getText());
    }

    @Test
    void m31() throws Exception {
        String json1 = "{\"id\":1,\"name\":\"1\"}";
        mvc.perform(post("/m31").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));

        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("修改数据", logInfo.getText());

        String json2 = "{\"id\":null,\"name\":\"1\"}";
        mvc.perform(post("/m31").contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));

        logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("新增数据", logInfo.getText());
    }
}

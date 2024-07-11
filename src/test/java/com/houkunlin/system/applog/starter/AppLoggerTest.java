package com.houkunlin.system.applog.starter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AppLoggerTest {
    public static final AppLogger APP_LOGGER = AppLoggerFactory.getLogger("test");

    @Test
    void log() {
        APP_LOGGER.log("Hello World");
        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("Hello World", logInfo.getText());
    }

    @Test
    void testLog() {
        APP_LOGGER.log("Hello World {}", 12);
        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("Hello World 12", logInfo.getText());
    }

    @Test
    void testLog1() {
        RuntimeException helloWorld = new RuntimeException("Hello World");
        APP_LOGGER.log("error {}", helloWorld);
        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("error Hello World", logInfo.getText());
        assertEquals(String.valueOf(helloWorld.hashCode()), logInfo.getExceptionCode());

        APP_LOGGER.log("在 {} 发生了错误 {}", "今天", helloWorld);
        logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("在 今天 发生了错误 Hello World", logInfo.getText());
        assertEquals(String.valueOf(helloWorld.hashCode()), logInfo.getExceptionCode());
    }

    @Test
    void logBiz() {
        APP_LOGGER.logBiz(1, "Hello World");
        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("Hello World", logInfo.getText());
        assertEquals(1, logInfo.getBusinessId());
    }

    @Test
    void testLogBiz() {
        APP_LOGGER.logBiz(2, "Hello World {}", 12);
        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("Hello World 12", logInfo.getText());
        assertEquals(2, logInfo.getBusinessId());
    }

    @Test
    void testLogBiz1() {
        RuntimeException helloWorld = new RuntimeException("Hello World");
        APP_LOGGER.logBiz(1, "Hello World {}", helloWorld);
        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("Hello World Hello World", logInfo.getText());
        assertEquals(1, logInfo.getBusinessId());
        assertEquals(String.valueOf(helloWorld.hashCode()), logInfo.getExceptionCode());

        APP_LOGGER.logBiz(1, "在 {} 发生了错误 {}", "今天", helloWorld);
        logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("在 今天 发生了错误 Hello World", logInfo.getText());
        assertEquals(1, logInfo.getBusinessId());
        assertEquals(String.valueOf(helloWorld.hashCode()), logInfo.getExceptionCode());
    }

    @Test
    void auditLog() {
        APP_LOGGER.auditLog(1, null, null, "Hello World {}", 12);
        AppLogInfo logInfo = Queue.QUEUE.poll();
        assertNotNull(logInfo);
        assertEquals("Hello World 12", logInfo.getText());
        assertEquals(1, logInfo.getBusinessId());
    }
}

package tests.application.common;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author HouKunLin
 */
@Component
public class TestBean {
    public String now() {
        return LocalDateTime.now().toString();
    }
}

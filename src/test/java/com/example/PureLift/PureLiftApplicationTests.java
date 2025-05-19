package com.example.PureLift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "spring.config.location=classpath:application-test.properties"
})
@ActiveProfiles("test")
class PureLiftApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
    }

}

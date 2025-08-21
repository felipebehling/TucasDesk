package com.example.tucasdesk;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

@SpringBootTest
@ActiveProfiles("test") // usa H2 durante os testes
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }
}

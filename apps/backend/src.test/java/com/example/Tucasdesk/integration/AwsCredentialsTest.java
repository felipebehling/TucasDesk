package com.example.Tucasdesk.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("docker")
public class AwsCredentialsTest {

    @Test
    void contextLoads() {
    }
}

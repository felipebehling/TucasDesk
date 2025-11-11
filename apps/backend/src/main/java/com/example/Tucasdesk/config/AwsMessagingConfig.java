package com.example.Tucasdesk.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Registers configuration properties related to AWS messaging.
 */
@Configuration
@EnableConfigurationProperties(AwsMessagingProperties.class)
public class AwsMessagingConfig {
}

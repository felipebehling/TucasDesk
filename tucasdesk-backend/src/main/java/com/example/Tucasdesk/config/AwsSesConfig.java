package com.example.Tucasdesk.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.SesV2ClientBuilder;

/**
 * Configuration that wires the AWS SES client when the feature is enabled.
 */
@Configuration
@EnableConfigurationProperties(AwsSesProperties.class)
public class AwsSesConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app.aws.ses", name = "enabled", havingValue = "true")
    public SesV2Client sesV2Client(AwsSesProperties properties) {
        Region region = (properties.getRegion() != null && !properties.getRegion().isBlank())
                ? Region.of(properties.getRegion())
                : null;
        SesV2ClientBuilder builder = SesV2Client.builder()
                .credentialsProvider(DefaultCredentialsProvider.create());
        if (region != null) {
            builder = builder.region(region);
        }
        return builder.build();
    }
}

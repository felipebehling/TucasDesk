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

    /**
     * Creates a {@link SesV2Client} bean if email notifications are enabled.
     * <p>
     * This bean is conditionally created only when {@code app.aws.ses.enabled} is {@code true}.
     * It configures the client with the specified AWS region.
     *
     * @param properties The AWS SES configuration properties.
     * @return A configured {@link SesV2Client} instance for sending emails.
     */
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

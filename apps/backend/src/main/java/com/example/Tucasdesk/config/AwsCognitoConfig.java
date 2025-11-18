package com.example.Tucasdesk.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

/**
 * Registers beans required to communicate with AWS Cognito.
 */
@Configuration
@EnableConfigurationProperties(AwsCognitoProperties.class)
public class AwsCognitoConfig {

    /**
     * Creates a {@link CognitoIdentityProviderClient} bean configured with the AWS region
     * specified in the application properties.
     *
     * @param properties The AWS Cognito configuration properties.
     * @return A configured {@link CognitoIdentityProviderClient} instance.
     */
    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClient(AwsCognitoProperties properties) {
        String region = properties.getRegion();
        if (region != null && !region.isBlank()) {
            return CognitoIdentityProviderClient.builder()
                    .region(Region.of(region))
                    .build();
        }
        return CognitoIdentityProviderClient.builder().build();
    }
}

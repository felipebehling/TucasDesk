package com.example.Tucasdesk.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for application metrics.
 * <p>
 * This class provides a fallback {@link MeterRegistry} for environments where
 * no specific metrics backend (like Prometheus) is configured.
 */
@Configuration
public class MetricsConfig {

    /**
     * Creates a {@link SimpleMeterRegistry} bean if no other {@link MeterRegistry}
     * bean is already present in the application context.
     * <p>
     * This is useful for development and testing, ensuring that metrics can be
     * collected without requiring a full monitoring stack.
     *
     * @return A {@link SimpleMeterRegistry} instance.
     */
    @Bean
    @ConditionalOnMissingBean(MeterRegistry.class)
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
}

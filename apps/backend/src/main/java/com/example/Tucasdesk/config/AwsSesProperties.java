package com.example.Tucasdesk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Configuration properties that control the integration with AWS Simple Email Service.
 */
@ConfigurationProperties(prefix = "app.aws.ses")
public class AwsSesProperties {

    /**
     * Flag that toggles the SES integration on or off. When disabled the application falls back to logging delivery.
     */
    private boolean enabled;

    /**
     * AWS region where the SES identities are configured.
     */
    private String region;

    /**
     * Source e-mail address (must match a verified identity in SES).
     */
    private String fromAddress;

    /**
     * Optional reply-to address.
     */
    private String replyToAddress;

    /**
     * Default list of recipients that should receive ticket notifications.
     */
    private final List<String> toAddresses = new ArrayList<>();

    /**
     * Name of the SES template used for rendering ticket notifications.
     */
    private String templateName;

    /**
     * Optional configuration set applied to outgoing e-mails (for analytics/monitoring in SES).
     */
    private String configurationSetName;

    /**
     * Charset used when building the e-mail subject/body for simple e-mails.
     */
    private String charset = "UTF-8";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getReplyToAddress() {
        return replyToAddress;
    }

    public void setReplyToAddress(String replyToAddress) {
        this.replyToAddress = replyToAddress;
    }

    public List<String> getToAddresses() {
        return Collections.unmodifiableList(toAddresses);
    }

    public void setToAddresses(List<String> addresses) {
        this.toAddresses.clear();
        if (addresses != null) {
            addresses.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(address -> !address.isBlank())
                    .forEach(this.toAddresses::add);
        }
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getConfigurationSetName() {
        return configurationSetName;
    }

    public void setConfigurationSetName(String configurationSetName) {
        this.configurationSetName = configurationSetName;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}

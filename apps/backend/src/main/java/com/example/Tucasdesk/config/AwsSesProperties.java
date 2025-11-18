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

    /**
     * @return {@code true} if SES is enabled, {@code false} otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled A flag to enable or disable SES.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return The AWS region for SES.
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region The AWS region for SES.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return The sender's email address.
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * @param fromAddress The sender's email address.
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * @return The reply-to email address.
     */
    public String getReplyToAddress() {
        return replyToAddress;
    }

    /**
     * @param replyToAddress The reply-to email address.
     */
    public void setReplyToAddress(String replyToAddress) {
        this.replyToAddress = replyToAddress;
    }

    /**
     * @return An unmodifiable list of recipient email addresses.
     */
    public List<String> getToAddresses() {
        return Collections.unmodifiableList(toAddresses);
    }

    /**
     * Sets the list of recipient addresses, filtering out any null or blank values.
     *
     * @param addresses The list of recipient email addresses.
     */
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

    /**
     * @return The name of the SES template.
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName The name of the SES template.
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return The name of the SES configuration set.
     */
    public String getConfigurationSetName() {
        return configurationSetName;
    }

    /**
     * @param configurationSetName The name of the SES configuration set.
     */
    public void setConfigurationSetName(String configurationSetName) {
        this.configurationSetName = configurationSetName;
    }

    /**
     * @return The character set for emails.
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset The character set for emails.
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }
}

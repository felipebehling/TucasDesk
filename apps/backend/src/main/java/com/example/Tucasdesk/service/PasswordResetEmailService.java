package com.example.Tucasdesk.service;

import com.example.Tucasdesk.config.AwsSesProperties;
import com.example.Tucasdesk.config.PasswordResetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.Body;
import software.amazon.awssdk.services.sesv2.model.Content;
import software.amazon.awssdk.services.sesv2.model.Destination;
import software.amazon.awssdk.services.sesv2.model.EmailContent;
import software.amazon.awssdk.services.sesv2.model.Message;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;
import software.amazon.awssdk.services.sesv2.model.SendEmailResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Handles the delivery of password reset e-mails through AWS SES.
 */
@Service
public class PasswordResetEmailService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetEmailService.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm")
            .withLocale(Locale.forLanguageTag("pt-BR"))
            .withZone(ZoneId.systemDefault());

    private final ObjectProvider<SesV2Client> sesV2ClientProvider;
    private final AwsSesProperties awsSesProperties;
    private final PasswordResetProperties passwordResetProperties;

    public PasswordResetEmailService(ObjectProvider<SesV2Client> sesV2ClientProvider,
                                     AwsSesProperties awsSesProperties,
                                     PasswordResetProperties passwordResetProperties) {
        this.sesV2ClientProvider = sesV2ClientProvider;
        this.awsSesProperties = awsSesProperties;
        this.passwordResetProperties = passwordResetProperties;
    }

    /**
     * Sends a password reset email to the specified recipient.
     *
     * @param recipient The email address of the recipient.
     * @param token     The password reset token.
     * @param expiresAt The expiration time of the token.
     */
    public void sendPasswordResetEmail(String recipient, String token, Instant expiresAt) {
        SesV2Client sesV2Client = sesV2ClientProvider.getIfAvailable();
        if (sesV2Client == null || !awsSesProperties.isEnabled()) {
            log.info("event=password_reset_email_skipped reason=ses_disabled recipient={} token={}",
                    recipient, token);
            return;
        }
        if (!StringUtils.hasText(awsSesProperties.getFromAddress())) {
            log.warn("event=password_reset_email_failed reason=from_address_missing recipient={}", recipient);
            return;
        }

        String resetLink = buildResetLink(token);
        String subject = passwordResetProperties.getEmailSubject();
        String expiration = DATE_TIME_FORMATTER.format(expiresAt);
        String textBody = buildTextBody(resetLink, expiration);
        String htmlBody = buildHtmlBody(resetLink, expiration);

        SendEmailRequest.Builder requestBuilder = SendEmailRequest.builder()
                .fromEmailAddress(awsSesProperties.getFromAddress())
                .destination(Destination.builder().toAddresses(recipient).build())
                .content(EmailContent.builder()
                        .simple(Message.builder()
                                .subject(Content.builder()
                                        .data(subject)
                                        .charset(awsSesProperties.getCharset())
                                        .build())
                                .body(Body.builder()
                                        .text(Content.builder()
                                                .data(textBody)
                                                .charset(awsSesProperties.getCharset())
                                                .build())
                                        .html(Content.builder()
                                                .data(htmlBody)
                                                .charset(awsSesProperties.getCharset())
                                                .build())
                                        .build())
                                .build())
                        .build());

        if (StringUtils.hasText(awsSesProperties.getReplyToAddress())) {
            requestBuilder = requestBuilder.replyToAddresses(awsSesProperties.getReplyToAddress());
        }
        if (StringUtils.hasText(awsSesProperties.getConfigurationSetName())) {
            requestBuilder = requestBuilder.configurationSetName(awsSesProperties.getConfigurationSetName());
        }

        try {
            SendEmailResponse response = sesV2Client.sendEmail(requestBuilder.build());
            log.info("event=password_reset_email_sent recipient={} messageId={}", recipient, response.messageId());
        } catch (AwsServiceException | SdkClientException ex) {
            log.error("event=password_reset_email_failed recipient={} reason={} message={}",
                    recipient, ex.getClass().getSimpleName(), ex.getMessage(), ex);
            throw ex;
        }
    }

    private String buildResetLink(String token) {
        String baseUrl = passwordResetProperties.getFrontendUrl();
        if (!StringUtils.hasText(baseUrl)) {
            baseUrl = "http://localhost:5173/redefinir-senha";
        }
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        if (baseUrl.contains("?")) {
            return baseUrl + "&token=" + encodedToken;
        }
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl + "?token=" + encodedToken;
    }

    private String buildTextBody(String resetLink, String expiration) {
        return "Olá,\n\n" +
                "Recebemos uma solicitação para redefinir sua senha no TucasDesk. " +
                "Caso você não tenha feito a solicitação, basta ignorar esta mensagem.\n\n" +
                "Para definir uma nova senha, acesse: " + resetLink + "\n\n" +
                "Este link expira em " + expiration + ".";
    }

    private String buildHtmlBody(String resetLink, String expiration) {
        return "<p>Olá,</p>" +
                "<p>Recebemos uma solicitação para redefinir sua senha no TucasDesk. " +
                "Caso você não tenha feito a solicitação, basta ignorar esta mensagem.</p>" +
                "<p>Para definir uma nova senha, clique no link abaixo:</p>" +
                "<p><a href='" + resetLink + "'>Redefinir minha senha</a></p>" +
                "<p>Este link expira em " + expiration + ".</p>";
    }
}

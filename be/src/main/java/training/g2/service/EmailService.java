package training.g2.service;

public interface EmailService {
    void sendEmailAsync(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    void sendActivationEmail(String to, String username, String activationLink);
}

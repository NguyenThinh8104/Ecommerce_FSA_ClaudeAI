package training.g2.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import training.g2.service.EmailService;

import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendEmailAsync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("ERROR SEND EMAIL: " + e.getMessage());
        }
    }

    public void sendUpdatePasswordEmail(String to, String username, String updateLink, int expireMinutes) {

        Context ctx = new Context();
        ctx.setVariable("username", username);
        ctx.setVariable("updateLink", updateLink);
        ctx.setVariable("expireMinutes", expireMinutes);

        String htmlContent = templateEngine.process("update-password", ctx);

        sendEmailAsync(to, "Cập nhật mật khẩu tài khoản", htmlContent, false, true);
    }

    public void sendActivationEmail(String to, String username, String activationLink) {
        Context ctx = new Context();
        ctx.setVariable("username", username);
        ctx.setVariable("activationLink", activationLink);

        String htmlContent = templateEngine.process("activate-account", ctx);
        sendEmailAsync(to, "Kích hoạt tài khoản", htmlContent, false, true);
    }
}
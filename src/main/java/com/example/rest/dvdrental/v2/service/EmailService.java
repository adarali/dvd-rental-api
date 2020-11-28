package com.example.rest.dvdrental.v2.service;

import com.example.rest.dvdrental.v2.entities.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${app.baseUrl:http://localhost:8080}")
    private String baseUrl;
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final HttpServletRequest request;
    
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, HttpServletRequest request) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.request = request;
    }
    
    public void sendVerificationEmail(AppUser user) {
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        variables.put("verificationUrl", getBaseUrl()+"/user/verify?code="+user.getVerificationCode());
        context.setVariables(variables);
        String text = templateEngine.process("email/verification", context);
        sendEmail(user.getEmail(), "Verification Email", text);
    }
    
    public void sendRecoveryEmail(AppUser user, String token) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("resetUrl", String.format("%s/user/recovery/%s/%s", getBaseUrl(), user.getUsername(), token));
        String text = templateEngine.process("email/recovery", context);
        sendEmail(user.getEmail(), "Password Reset", text);
    }
    
    private void sendEmail(String to, String subject, String text) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
//            helper.setFrom(fromEmail, "ABC DVD Rental Services");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
        };
        mailSender.send(messagePreparator);
    }
    
    private String getBaseUrl() {
        return this.baseUrl;
    }
}

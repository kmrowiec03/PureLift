package com.example.PureLift.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.example.PureLift.messaging.RabbitMQConfig.EMAIL_QUEUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {
    private final JavaMailSender mailSender;

    @RabbitListener(queues = EMAIL_QUEUE)
    public void handleEmail(EmailMessage message) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(message.getTo());
            email.setSubject(message.getSubject());
            email.setText(message.getBody());
            mailSender.send(email);
            log.info("Email sent successfully to: {}", message.getTo());
        } catch (MailException e) {
            log.warn("Failed to send email to: {} - {}", message.getTo(), e.getMessage());
            log.debug("Email details - Subject: {}, Body: {}", message.getSubject(), message.getBody());
        }
    }
}
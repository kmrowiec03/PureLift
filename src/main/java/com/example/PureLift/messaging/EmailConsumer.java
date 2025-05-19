package com.example.PureLift.messaging;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.example.PureLift.messaging.RabbitMQConfig.EMAIL_QUEUE;

@Service
@RequiredArgsConstructor
public class EmailConsumer {
    private final JavaMailSender mailSender;

    @RabbitListener(queues = EMAIL_QUEUE)
    public void handleEmail(EmailMessage message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(message.getTo());
        email.setSubject(message.getSubject());
        email.setText(message.getBody());
        mailSender.send(email);
    }
}
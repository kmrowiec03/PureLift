package com.example.PureLift.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.example.PureLift.messaging.RabbitMQConfig.EMAIL_QUEUE;

@Service
@RequiredArgsConstructor
public class EmailProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailMessage emailMessage) {
        rabbitTemplate.convertAndSend(EMAIL_QUEUE, emailMessage);
    }
}
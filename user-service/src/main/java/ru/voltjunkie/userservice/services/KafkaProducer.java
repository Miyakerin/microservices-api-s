package ru.voltjunkie.userservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.voltjunkie.userservice.dto.EmailDto;

@Service
public class KafkaProducer {

    @Autowired
    private final KafkaTemplate<String, EmailDto> emailKafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, EmailDto> emailKafkaTemplate) {
        this.emailKafkaTemplate = emailKafkaTemplate;
    }

    public void send(String topic, EmailDto emailDto) {
        Message<EmailDto> message = MessageBuilder.withPayload(emailDto).setHeader(KafkaHeaders.TOPIC, topic).build();
        this.emailKafkaTemplate.send(message);
    }
}

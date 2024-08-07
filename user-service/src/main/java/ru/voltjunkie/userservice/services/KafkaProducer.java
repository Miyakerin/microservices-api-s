package ru.voltjunkie.userservice.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.voltjunkie.userservice.dto.EmailDto;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    @Value("${topic.send-order}")
    private String topic;

    @Autowired
    private final KafkaTemplate<String, Object> emailKafkaTemplate;


    public void send(EmailDto emailDto) {
        emailKafkaTemplate.send(topic, emailDto);
    }
}

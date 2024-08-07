package ru.voltjunkie.notificationservice.services;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.voltjunkie.notificationservice.dto.EmailDto;

import java.sql.Timestamp;
import java.util.Date;

@Service
@AllArgsConstructor
public class KafkaConsumer {
    private static final String topicEmailRegister = "${topic.send-order}";
    private static final String kafkaConsumerId = "${spring.kafka.consumer.group-id}";
    private final EmailSenderService emailSenderService;


    @Transactional
    @KafkaListener(topics = topicEmailRegister, groupId = kafkaConsumerId, properties = {"spring.json.value.default.type=ru.voltjunkie.notificationservice.dto.EmailDto"})
    public void listen(@Payload EmailDto emailDto, @Headers MessageHeaders headers) {
        emailSenderService.sendEmail(emailDto);
    }
}

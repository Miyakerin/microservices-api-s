package ru.voltjunkie.notificationservice.services;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.voltjunkie.notificationservice.dto.EmailDto;

import java.sql.Timestamp;
import java.util.Date;

@Service
@AllArgsConstructor
public class KafkaConsumer {
    private final EmailSenderService emailSenderService;


    @KafkaListener(topics = "emailRegistrationTopic", groupId = "group1", containerFactory = "emailKafkaListenerContainerFactory")
    public void listen(@Payload EmailDto emailDto, @Headers MessageHeaders headers) {
        emailSenderService.sendEmail(emailDto);
    }
}

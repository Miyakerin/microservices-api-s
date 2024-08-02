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
import ru.voltjunkie.notificationservice.store.entities.LinkEntity;
import ru.voltjunkie.notificationservice.store.repositories.LinksRepository;

import java.sql.Timestamp;
import java.util.Date;

@Service
@AllArgsConstructor
public class KafkaConsumer {
    private final EmailSenderService emailSenderService;
    private final LinksRepository linksRepository;


    @KafkaListener(topics = "emailRegistrationTopic", groupId = "group1", containerFactory = "emailKafkaListenerContainerFactory")
    public void listen(@Payload EmailDto emailDto, @Headers MessageHeaders headers) {
        LinkEntity link_user = LinkEntity.builder()
                .userId(emailDto.getUser_id())
                .exp(new Timestamp(System.currentTimeMillis()*1000*60*20))
                .build();
        //to-do place link for email-confirmation
        //emailDto.setBody();
        linksRepository.save(link_user);
        emailSenderService.sendEmail(emailDto);
    }
}

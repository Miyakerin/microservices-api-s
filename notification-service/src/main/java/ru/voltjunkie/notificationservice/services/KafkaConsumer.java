package ru.voltjunkie.notificationservice.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "emailRegistrationTopic", groupId = "group1")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println(record.key() + ": " + record.value() + "at group1");
    }
}

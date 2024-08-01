package ru.voltjunkie.notificationservice.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "notificationTopic", groupId = "notificationId")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println(record.value());
    }
}

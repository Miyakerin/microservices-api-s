package ru.voltjunkie.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.voltjunkie.userservice.kafka.KafkaProducer;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaProducer kafkaProducer;



    @GetMapping
    public String test() {
        kafkaProducer.send("notificationTopic", "test");
        return "Hello World";
    }
}

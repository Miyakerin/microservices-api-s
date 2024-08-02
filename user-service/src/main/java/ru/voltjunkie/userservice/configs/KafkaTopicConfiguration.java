package ru.voltjunkie.userservice.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic newEmailRegistrationTopic() {
        return new NewTopic("emailRegistrationTopic",
                1,
                (short) 1);
    }
}

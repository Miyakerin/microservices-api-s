package ru.voltjunkie.notificationservice.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.voltjunkie.notificationservice.store.entities.LinkEntity;
import ru.voltjunkie.notificationservice.store.repositories.LinksRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailService {
    private final LinksRepository linksRepository;
    private final RestTemplate restTemplate;

    //to-do: somehow bypass token verification in user-service and proceed with it
    public Boolean confirmRegistration(String uuid) {
        LinkEntity linkEntity = linksRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        HttpHeaders headers = new HttpHeaders();
        String token = "";
        headers.set("Authorization", "Bearer " + token);
        MultiValueMap<String, Boolean> params = new LinkedMultiValueMap<>();
        params.add("isEmailConfirmed", true);
        //restTemplate.patchForObject("http://user-service/api/users/" + linkEntity.getUserId(), );
        return true;
    }
}

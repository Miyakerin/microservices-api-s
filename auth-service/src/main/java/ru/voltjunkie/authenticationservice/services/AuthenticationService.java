package ru.voltjunkie.authenticationservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.voltjunkie.authenticationservice.dto.UserDto;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public UserDto authenticate(UserDto userDto) {
    }

    public UserDto register(UserDto userDto) {
    }
}

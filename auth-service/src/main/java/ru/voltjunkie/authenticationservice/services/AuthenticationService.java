package ru.voltjunkie.authenticationservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.voltjunkie.authenticationservice.entities.AuthenticateRequest;
import ru.voltjunkie.authenticationservice.entities.RegisterRequest;
import ru.voltjunkie.authenticationservice.entities.TokensResponse;
import ru.voltjunkie.authenticationservice.entities.UserDto;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public TokensResponse authenticate(AuthenticateRequest request) {
        UserDto userDto = restTemplate.postForObject("http://user-service/api/users/authenticate", request, UserDto.class);

        String accessToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "REFRESH");

        return new TokensResponse(accessToken, refreshToken);
    }

    public TokensResponse register(RegisterRequest request) {
        UserDto userDto = restTemplate.postForObject("http://user-service/api/users", request, UserDto.class);

        String accessToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "REFRESH");

        return new TokensResponse(accessToken, refreshToken);
    }
}

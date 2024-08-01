package ru.voltjunkie.authenticationservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.voltjunkie.authenticationservice.entities.AuthenticateRequest;
import ru.voltjunkie.authenticationservice.entities.RegisterRequest;
import ru.voltjunkie.authenticationservice.entities.TokensResponse;
import ru.voltjunkie.authenticationservice.entities.UserDto;

import java.security.interfaces.RSAPublicKey;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public TokensResponse authenticate(String username, String password) {
        return null;
    }

    public TokensResponse register(String username, String password) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("username", username);
        params.add("password", password);

        UserDto userDto = restTemplate.postForObject("http://user-service/api/users", params, UserDto.class);

        String accessToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "REFRESH");

        return new TokensResponse(accessToken, refreshToken);
    }

    public TokensResponse refreshToken(String token) {
        System.out.println(jwtUtil.getAllClaims(token));
        System.out.println(jwtUtil.verifyToken(token));
        return null;
    }

    public String getPublicKey() {
        return jwtUtil.getPublicKey();
    }
}

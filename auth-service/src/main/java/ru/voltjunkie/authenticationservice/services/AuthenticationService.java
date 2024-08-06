package ru.voltjunkie.authenticationservice.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.voltjunkie.authenticationservice.entities.AuthenticateRequest;
import ru.voltjunkie.authenticationservice.entities.RegisterRequest;
import ru.voltjunkie.authenticationservice.entities.TokensResponse;
import ru.voltjunkie.authenticationservice.entities.UserDto;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public TokensResponse authenticate(String username, String password) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("username", username);
        params.add("password", password);

        UserDto userDto = restTemplate.postForObject("http://user-service/api/users/authenticate", params, UserDto.class);

        String accessToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "REFRESH");

        return new TokensResponse(accessToken, refreshToken);
    }

    public TokensResponse register(String username, String password, String email) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("username", username);
        params.add("password", password);
        params.add("email", email);

        UserDto userDto = restTemplate.postForObject("http://user-service/api/users", params, UserDto.class);

        String accessToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "REFRESH");

        return new TokensResponse(accessToken, refreshToken);
    }

    public TokensResponse refreshToken(String token) {
        try {
            jwtUtil.verifyToken(token);
        } catch (JWTVerificationException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        Map<String, Claim> claims = jwtUtil.getAllClaims(token);
        if (!claims.get("tokenType").asString().equals("REFRESH")) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "token is invalid");
        }

        String serverToken = jwtUtil.generateToken((long) 0, "ADMIN", "ACCESS");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serverToken);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        UserDto userDto = restTemplate.exchange("http://user-service/api/users/" + claims.get("sub").asString(),
                HttpMethod.GET,
                request,
                UserDto.class).getBody();

        String accessToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generateToken(userDto.getId(), userDto.getRole(), "REFRESH");

        return new TokensResponse(accessToken, refreshToken);
    }

    public String getPublicKey() {
        return jwtUtil.getPublicKey();
    }
}

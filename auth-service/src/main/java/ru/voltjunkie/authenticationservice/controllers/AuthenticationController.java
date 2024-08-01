package ru.voltjunkie.authenticationservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.voltjunkie.authenticationservice.entities.AuthenticateRequest;
import ru.voltjunkie.authenticationservice.entities.RegisterRequest;
import ru.voltjunkie.authenticationservice.entities.TokensResponse;
import ru.voltjunkie.authenticationservice.entities.UserDto;
import ru.voltjunkie.authenticationservice.services.AuthenticationService;

import java.security.interfaces.RSAPublicKey;

@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private static final String authenticatePath = "/jwt/authenticate";
    private static final String registerPath = "/jwt/register";
    private static final String refreshPath = "/jwt/refresh";
    private static final String publicKeyPath = "/jwt/publicKey";

    private final AuthenticationService authenticationService;

    @PostMapping(value = authenticatePath)
    public ResponseEntity<TokensResponse> authenticate(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(authenticationService.authenticate(username, password));
    }

    @PostMapping(value = registerPath)
    public ResponseEntity<TokensResponse> register(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(authenticationService.register(username, password));
    }

    @PostMapping(value = refreshPath)
    public ResponseEntity<TokensResponse> refreshTokens(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }

    @GetMapping(publicKeyPath)
    public ResponseEntity<String> getPublicKey() {
        return ResponseEntity.ok(authenticationService.getPublicKey());
    }

}

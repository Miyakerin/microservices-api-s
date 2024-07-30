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

@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private static final String authenticatePath = "";
    private static final String registerPath = "/register";

    private final AuthenticationService authenticationService;

    @PostMapping(value = authenticatePath)
    public ResponseEntity<TokensResponse> authenticate(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping(value = registerPath)
    public ResponseEntity<TokensResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

}

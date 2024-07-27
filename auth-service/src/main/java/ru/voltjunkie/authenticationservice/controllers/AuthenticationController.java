package ru.voltjunkie.authenticationservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.voltjunkie.authenticationservice.dto.UserDto;
import ru.voltjunkie.authenticationservice.services.AuthenticationService;

@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private static final String authenticatePath = "";
    private static final String registerPath = "/register";

    private final AuthenticationService authenticationService;

    @PostMapping(value = authenticatePath)
    public ResponseEntity<UserDto> authenticate(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authenticationService.authenticate(userDto));
    }

    @PostMapping(value = registerPath)
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authenticationService.register(userDto));
    }
}

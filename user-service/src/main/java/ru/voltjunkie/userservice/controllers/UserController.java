package ru.voltjunkie.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.voltjunkie.userservice.dto.UserDto;
import ru.voltjunkie.userservice.exceptions.BadRequestException;
import ru.voltjunkie.userservice.services.UserService;

@RestController
@RequestMapping(value = "/api/users")
@AllArgsConstructor
public class UserController {
    private static final String saveUserPath = "";
    private static final String authenticateUserPath = "/authenticate";

    private final UserService userService;

    @PostMapping(value = saveUserPath)
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.save(userDto));

    }

    @PostMapping(value = authenticateUserPath)
    public ResponseEntity<UserDto> authenticateUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.authenticate(userDto));
    }

}

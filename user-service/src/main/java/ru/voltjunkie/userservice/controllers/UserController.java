package ru.voltjunkie.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.voltjunkie.userservice.dto.UserDto;
import ru.voltjunkie.userservice.exceptions.BadRequestException;
import ru.voltjunkie.userservice.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/users")
@AllArgsConstructor
public class UserController {
    private static final String createUserPath = "";
    private static final String deleteUserPath = "";
    private static final String updateUserPath = "";
    private static final String getUserPath = "";
    private static final String authenticateUserPath = "/authenticate"; // rename in future?

    private final UserService userService;

    @PostMapping(value = authenticateUserPath)
    public ResponseEntity<UserDto> authenticateUser(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.authenticate(username, password));
    }

    @PostMapping(value = createUserPath)
    public ResponseEntity<UserDto> saveUser(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.save(email, username, password));

    }

    @GetMapping(value = getUserPath)
    public ResponseEntity<List<UserDto>> getUsers(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getUsers(token));
    }

    @GetMapping(value = getUserPath + "/{userId}")
    public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token, @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUser(userId, token));
    }

    @PatchMapping(value = updateUserPath + "/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestHeader("Authorization") String token,
                                              @PathVariable("userId") Long userId,
                                              @RequestParam Optional<String> username, @RequestParam Optional<String> email,
                                              @RequestParam Optional<Boolean> isEmailConfirmed,
                                              @RequestParam Optional<String> password, @RequestParam Optional<String> role) {
        return ResponseEntity.ok(userService.updateUser(token, userId, username, email, isEmailConfirmed, password, role));
    }

    @DeleteMapping(value = deleteUserPath + "/{userId}")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader("Authorization") String token,
                                              @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.deleteUser(token, userId));
    }

}

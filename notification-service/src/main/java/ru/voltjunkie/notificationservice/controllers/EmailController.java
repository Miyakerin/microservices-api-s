package ru.voltjunkie.notificationservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.voltjunkie.notificationservice.services.EmailService;

@RestController
@RequestMapping(value = "/api/mail")
@AllArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping(value = "/confirmation")
    public ResponseEntity<Boolean> confirmEmail(@RequestParam String uuid) {
        return ResponseEntity.ok(emailService.confirmRegistration(uuid));
    }
}

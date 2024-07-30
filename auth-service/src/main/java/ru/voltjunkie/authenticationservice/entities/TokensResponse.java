package ru.voltjunkie.authenticationservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokensResponse {
    private String accessToken;
    private String refreshToken;
}

package ru.voltjunkie.authenticationservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private int id;
    private String username;
    private String password;
    private String role;
}
package ru.voltjunkie.userservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {
    private String email;
    private String subject;
    private String body;
    private Long user_id;
}

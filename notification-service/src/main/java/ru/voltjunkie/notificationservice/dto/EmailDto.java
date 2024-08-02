package ru.voltjunkie.notificationservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {
    private String email;
    private String subject;
    private String body;
    private Long user_id;
}

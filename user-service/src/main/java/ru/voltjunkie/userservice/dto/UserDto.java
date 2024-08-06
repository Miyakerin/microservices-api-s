package ru.voltjunkie.userservice.dto;

import lombok.*;
import ru.voltjunkie.userservice.store.entites.UserEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
    private Boolean isEmailConfirmed;
    private Boolean isDeleted;

    public static UserDto toDto(UserEntity user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setIsEmailConfirmed(user.getIsEmailConfirmed());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        userDto.setIsDeleted(user.getIsDeleted());
        return userDto;
    }
}

package ru.voltjunkie.userservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.voltjunkie.userservice.dto.UserDto;
import ru.voltjunkie.userservice.exceptions.BadRequestException;
import ru.voltjunkie.userservice.store.entites.UserEntity;
import ru.voltjunkie.userservice.store.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto save(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        final UserEntity userEntity = userRepository.saveAndFlush(
                UserEntity.builder()
                        .username(userDto.getUsername())
                        .password(userDto.getPassword()) //to-do change on Crypt
                        .role("USER")
                        .build()
        );
        userDto.setRole(userEntity.getRole());
        userDto.setId(userEntity.getId());
        return userDto;
    }
}

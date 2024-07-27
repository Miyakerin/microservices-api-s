package ru.voltjunkie.userservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.voltjunkie.userservice.dto.UserDto;
import ru.voltjunkie.userservice.exceptions.BadRequestException;
import ru.voltjunkie.userservice.store.entites.UserEntity;
import ru.voltjunkie.userservice.store.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
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
                        .password(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()))
                        .role("USER")
                        .build()
        );
        userDto.setRole(userEntity.getRole());
        userDto.setId(userEntity.getId());
        return userDto;
    }

    public UserDto authenticate(UserDto userDto) {
        UserEntity userEntity = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new BadRequestException("Username or password is incorrect"));

        if (!BCrypt.checkpw(userDto.getPassword(), userEntity.getPassword())) {
            userDto.setRole("USER");
            userDto.setId(userEntity.getId());
            return userDto;
        }

        throw new BadRequestException("Username or password is incorrect");
    }
}

package ru.voltjunkie.userservice.services;

import com.auth0.jwt.interfaces.Claim;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.voltjunkie.userservice.dto.UserDto;
import ru.voltjunkie.userservice.exceptions.BadRequestException;
import ru.voltjunkie.userservice.store.entites.UserEntity;
import ru.voltjunkie.userservice.store.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserDto save(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username is already taken");
        }
        final UserEntity userEntity = userRepository.saveAndFlush(
                UserEntity.builder()
                        .username(username)
                        .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                        .role("USER")
                        .build()
        );

        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .role(userEntity.getRole())
                .password(userEntity.getPassword())
                .build();
    }

    public UserDto authenticate(String username, String password) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("Username or password is incorrect"));

        if (BCrypt.checkpw(password, userEntity.getPassword())) {
            return UserDto.builder()
                    .id(userEntity.getId())
                    .username(userEntity.getUsername())
                    .role(userEntity.getRole())
                    .password(userEntity.getPassword())
                    .build();
        }

        throw new BadRequestException("Username or password is incorrect");
    }

    public UserDto getUser(Long userId, String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "token not valid");
        }
        Map<String, Claim> claims = jwtUtil.getAllClaims(token);
        if (Long.parseLong(claims.get("sub").asString()) == userId || claims.get("role").asString().equals("ADMIN")) {
            return UserDto.toDto(userRepository.findById(userId).orElseThrow(() -> new BadRequestException("user not found")));
        }
        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Not enough permissions");
    }

    public List<UserDto> getUsers(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "token not valid");
        }
        Map<String, Claim> claims = jwtUtil.getAllClaims(token);
        if (claims.get("role").asString().equals("ADMIN")) {
            return userRepository.findAll().stream().map(UserDto::toDto).toList();
        }
        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Not enough permissions");
    }


    public UserDto updateUser(String token, Long userId, Optional<String> username, Optional<String> password, Optional<String> role) {
        if (!jwtUtil.validateToken(token)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "token not valid");
        }
        Map<String, Claim> claims = jwtUtil.getAllClaims(token);
        if (Long.parseLong(claims.get("sub").asString()) == userId || claims.get("role").asString().equals("ADMIN")) {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("user not found"));
            userEntity.setUsername(username.orElse(userEntity.getUsername()));
            userEntity.setPassword(password.map(x -> BCrypt.hashpw(x, BCrypt.gensalt())).orElse(userEntity.getPassword()));
            if (claims.get("role").asString().equals("ADMIN")) {
                userEntity.setRole(role.orElse(userEntity.getRole()));
            }
            return UserDto.toDto(userRepository.save(userEntity));
        }
        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Not enough permissions");
    }

    public Boolean deleteUser(String token, Long userId) {
        if (!jwtUtil.validateToken(token)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "token not valid");
        }
        Map<String, Claim> claims = jwtUtil.getAllClaims(token);
        if (Long.parseLong(claims.get("sub").asString()) == userId || claims.get("role").asString().equals("ADMIN")) {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("user not found"));
            userRepository.delete(userEntity);
            return true;
        }
        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Not enough permissions");
    }
}

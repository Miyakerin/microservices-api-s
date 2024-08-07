package ru.voltjunkie.userservice.services;

import com.auth0.jwt.interfaces.Claim;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.voltjunkie.userservice.dto.EmailDto;
import ru.voltjunkie.userservice.dto.UserDto;
import ru.voltjunkie.userservice.exceptions.BadRequestException;
import ru.voltjunkie.userservice.store.entites.MailEntity;
import ru.voltjunkie.userservice.store.entites.UserEntity;
import ru.voltjunkie.userservice.store.repositories.MailRepository;
import ru.voltjunkie.userservice.store.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final KafkaProducer kafkaProducer;
    private final MailRepository mailRepository;

    public UserDto save(String email, String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already taken");
        }
        final UserEntity userEntity = userRepository.save(
                UserEntity.builder()
                        .username(username)
                        .email(email)
                        .isEmailConfirmed(false)
                        .isDeleted(false)
                        .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                        .role("USER")
                        .build()
        );

        MailEntity mailEntity = MailEntity.builder()
                .user(userEntity)
                .exp(new Timestamp(System.currentTimeMillis() + 1000*60*5))
                .build();
        mailRepository.save(mailEntity);

        kafkaProducer.send(EmailDto.builder().email(userEntity.getEmail()).subject("confirmation")
                        .body("uuid - " + mailEntity.getId().toString()).user_id(userEntity.getId()).build());

        return UserDto.toDto(userEntity);
    }

    public UserDto authenticate(String username, String password) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("Username or password is incorrect"));

        if (!userEntity.getIsEmailConfirmed()) {
            throw new BadRequestException("Email not confirmed");
        }

        if (userEntity.getIsDeleted()) {
            throw new BadRequestException("User is deleted");
        }

        if (BCrypt.checkpw(password, userEntity.getPassword())) {
            return UserDto.toDto(userEntity);
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


    public UserDto updateUser(String token,
                              Long userId,
                              Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted,
                              Optional<Boolean> isEmailConfirmed, Optional<String> password, Optional<String> role) {
        if (!jwtUtil.validateToken(token)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "token not valid");
        }

        Map<String, Claim> claims = jwtUtil.getAllClaims(token);
        if (Long.parseLong(claims.get("sub").asString()) == userId || claims.get("role").asString().equals("ADMIN")) {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("user not found"));
            userEntity.setUsername(username.orElse(userEntity.getUsername()));
            if (email.isPresent() && !email.get().equalsIgnoreCase(userEntity.getEmail())) {
                userEntity.setEmail(email.orElse(userEntity.getEmail()));
                userEntity.setIsEmailConfirmed(false);

                MailEntity mailEntity = MailEntity.builder()
                                .user(userEntity)
                                .exp(new Timestamp(System.currentTimeMillis() + 1000*60*5))
                                .build();
                mailRepository.save(mailEntity);

                kafkaProducer.send(EmailDto.builder().email(userEntity.getEmail()).subject("confirmation")
                                .body("uuid - " + mailEntity.getId().toString()).user_id(userEntity.getId()).build());
            }
            userEntity.setIsDeleted(isDeleted.orElse(userEntity.getIsDeleted()));
            userEntity.setPassword(password.map(x -> BCrypt.hashpw(x, BCrypt.gensalt())).orElse(userEntity.getPassword()));

            if (claims.get("role").asString().equals("ADMIN")) {
                userEntity.setRole(role.orElse(userEntity.getRole()));
                userEntity.setIsEmailConfirmed(isEmailConfirmed.orElse(userEntity.getIsEmailConfirmed()));
            }
            userRepository.save(userEntity);
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
            userEntity.setIsDeleted(true);
            userRepository.save(userEntity);
            return true;
        }
        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Not enough permissions");
    }

    public Boolean confirmEmail(UUID emailId) {
        MailEntity mailEntity = mailRepository.findById(emailId).orElseThrow(() -> new BadRequestException("mail not found"));
        if (new Timestamp(System.currentTimeMillis()).before(mailEntity.getExp())) {
            UserEntity userEntity = mailEntity.getUser();
            userEntity.setIsEmailConfirmed(true);
            userRepository.save(userEntity);
            return true;
        }
        return false;

    }
}

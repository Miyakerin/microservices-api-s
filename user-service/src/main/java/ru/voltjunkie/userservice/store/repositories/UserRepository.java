package ru.voltjunkie.userservice.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.voltjunkie.userservice.store.entites.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findById(Long id);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

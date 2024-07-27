package ru.voltjunkie.userservice.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.voltjunkie.userservice.store.entites.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findById(Integer id);

    Boolean existsByUsername(String username);
}

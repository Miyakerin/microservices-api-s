package ru.voltjunkie.userservice.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.voltjunkie.userservice.store.entites.MailEntity;

import java.util.UUID;

public interface MailRepository extends JpaRepository<MailEntity, UUID> {
}

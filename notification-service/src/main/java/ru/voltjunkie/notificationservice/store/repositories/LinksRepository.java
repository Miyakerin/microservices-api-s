package ru.voltjunkie.notificationservice.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.voltjunkie.notificationservice.store.entities.LinkEntity;

import java.util.Optional;
import java.util.UUID;

public interface LinksRepository extends JpaRepository<LinkEntity, UUID> {

}

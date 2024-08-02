package ru.voltjunkie.notificationservice.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="links")
public class LinkEntity {
    @Id
    @Column(nullable = false, unique = true, name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = true, unique = false, name = "user_id")
    private Long userId;
    @Column(nullable = false, unique = false, name = "exp")
    private Timestamp exp;
}

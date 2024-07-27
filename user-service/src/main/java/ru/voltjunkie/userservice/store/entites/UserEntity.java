package ru.voltjunkie.userservice.store.entites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="client")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "id")
    private int id;
    @Column(nullable = false, unique = true, name = "username")
    private String username;
    @Column(nullable = false, unique = false, name = "password")
    private String password;
    @Column(nullable = false, unique = false, name = "role")
    private String role;
}
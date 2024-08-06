package ru.voltjunkie.userservice.store.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private Long id;
    @Column(nullable = false, unique = true, name = "username")
    private String username;
    @Column(nullable = false, unique = false, name = "password")
    private String password;
    @Column(nullable = false, unique = false, name = "role")
    private String role;
    @Column(nullable = false, unique = true, name = "email")
    private String email;
    @Column(nullable = false, unique = false, name = "is_email_confirmed")
    private Boolean isEmailConfirmed;
    @Column(nullable = false, unique = false, name = "is_deleted")
    private Boolean isDeleted;
}
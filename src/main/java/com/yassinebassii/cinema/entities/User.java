package com.yassinebassii.cinema.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "first_name", length = 50)
    String firstName;
    @Column(name = "last_name", length = 50)
    String lastName;
    @Column(length = 50, nullable = false, unique = true)
    String email;
    String password;
    boolean active;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
                name = "user_id",
                referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
                name = "role_id",
                referencedColumnName = "id"
        )
    )
    Collection<Role> roles;
}

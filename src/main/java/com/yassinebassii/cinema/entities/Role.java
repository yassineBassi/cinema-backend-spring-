package com.yassinebassii.cinema.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Role{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 20)
    String role;
}

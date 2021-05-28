package com.yassinebassii.cinema.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString(exclude = {"cinemas"})
public class Ville extends com.yassinebassii.cinema.entities.Data implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String name;
    private double longitude, latitude, altitude;
    @OneToMany(mappedBy = "ville")
    private List<Cinema> cinemas;
}

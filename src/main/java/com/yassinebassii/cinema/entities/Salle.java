package com.yassinebassii.cinema.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor @ToString @Data
public class Salle implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 12)
    private String name;
    @ManyToOne
    private Cinema cinema;
    @OneToMany(mappedBy = "salle")
    private List<Place> places;
    @OneToMany(mappedBy = "salle")
    private List<Projection> projections;
}

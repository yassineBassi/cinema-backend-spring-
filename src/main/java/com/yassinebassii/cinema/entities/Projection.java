package com.yassinebassii.cinema.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Projection implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private double price;
    @ManyToOne
    private Film film;
    @ManyToOne
    private Salle salle;
    @OneToMany(mappedBy = "projection")
    private List<Ticket> tickets;
    @ManyToOne
    private Seance seance;
}

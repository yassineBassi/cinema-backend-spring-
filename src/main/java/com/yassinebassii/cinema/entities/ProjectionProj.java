package com.yassinebassii.cinema.entities;

import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name="p1", types = {com.yassinebassii.cinema.entities.Projection.class})
public interface ProjectionProj {
    Long getId();
    double getPrix();
    Film getFilm();
    Seance getSeance();
    List<TicketProj> getTickets();
//    public

}

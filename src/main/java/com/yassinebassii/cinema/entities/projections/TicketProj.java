package com.yassinebassii.cinema.entities.projections;

import com.yassinebassii.cinema.entities.Place;
import com.yassinebassii.cinema.entities.Ticket;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.web.bind.annotation.CrossOrigin;

@Projection(name="p1", types = {Ticket.class})
public interface TicketProj {
    int getId();
    double getPrice();
    Integer getCodePayment();
    boolean getReserve();
    Place getPlace();
}

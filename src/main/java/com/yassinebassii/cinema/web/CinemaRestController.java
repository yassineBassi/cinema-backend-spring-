package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.CinemaRepository;
import com.yassinebassii.cinema.dao.FilmRepository;
import com.yassinebassii.cinema.dao.TicketRepository;
import com.yassinebassii.cinema.entities.Cinema;
import com.yassinebassii.cinema.entities.Film;
import com.yassinebassii.cinema.entities.Ticket;
import com.yassinebassii.cinema.entities.Ville;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@Transactional
@CrossOrigin("*")
public class CinemaRestController {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    FilmRepository filmRepository;

    @GetMapping(path = "/films/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable(name = "id") Long id) throws IOException {
        Film film = filmRepository.findById(id).get();
        File file = new File(System.getProperty("user.home") + "/cinema/images/" + film.getPhoto());
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }

    @PostMapping(path = "/tickets/payer")
    public void payerTicket(@RequestBody TicketForm ticketForm){
        ticketForm.getTickets().forEach(ticketId -> {
            Ticket ticket = ticketRepository.findById(ticketId).get();
            ticket.setReserve(true);
            ticket.setClientName(ticketForm.getClientName());
            ticket.setCodePayment(ticketForm.getCodePayment());
            ticketRepository.save(ticket);
        });
    }

}

@Data
class  TicketForm{
    private String clientName;
    private int codePayment;
    private List<Long> tickets;
}

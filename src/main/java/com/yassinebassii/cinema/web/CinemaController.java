package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.FilmRepository;
import com.yassinebassii.cinema.dao.TicketRepository;
import com.yassinebassii.cinema.entities.Film;
import com.yassinebassii.cinema.entities.Ticket;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@Transactional
@CrossOrigin("*")
public class CinemaController {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    FilmRepository filmRepository;

    @GetMapping(path = "/films/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable(name = "id") Long id) throws IOException {
        Film film = filmRepository.findById(id).get();
        File file = new File(System.getProperty("user.home") + "/cinema/images/" + film.getPhoto() + ".jpg");
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }

    @PostMapping(path = "/tickets/payer")
    public void payerTicket(@RequestBody TicketForm ticketForm){
        ticketForm.getTickets().forEach(ticketId -> {
            Ticket ticket = ticketRepository.findById(ticketId).get();
            ticket.setReserve(true);
            ticket.setNomClient(ticketForm.getNomClient());
            ticket.setCodePayement(ticketForm.getCodePayement());
            ticketRepository.save(ticket);
        });
    }

    @GetMapping(path = "/dashboard")
    public String home(HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        AtomicBoolean isAdmin = new AtomicBoolean(false);
        authentication.getAuthorities().forEach(grantedAuthority -> {
            if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                isAdmin.set(true);
            }
        });
        if(isAdmin.get()) return "home";
        httpServletResponse.sendRedirect("/login?error");
        return "";
    }

}

@Data
class  TicketForm{
    private String nomClient;
    private int codePayement;
    private List<Long> tickets;
}

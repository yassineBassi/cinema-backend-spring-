package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.CinemaRepository;
import com.yassinebassii.cinema.dao.ProjectionRepository;
import com.yassinebassii.cinema.dao.TicketRepository;
import com.yassinebassii.cinema.dao.VilleRepository;
import com.yassinebassii.cinema.entities.Cinema;
import com.yassinebassii.cinema.entities.Projection;
import com.yassinebassii.cinema.entities.Ticket;
import com.yassinebassii.cinema.entities.Ville;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class TicketController {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    ProjectionRepository projectionRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public TicketController() {
        attributes.add(new Attribute("id", "#", "number", false, null));
        attributes.add(new Attribute("placeNumber", "Place", "number", false, null));
        attributes.add(new Attribute("price", "Prix", "number", true, null));
        attributes.add(new Attribute("clientName", "Nom de client", "text", true, null));
        attributes.add(new Attribute("codePayment", "codePayment", "number", true, null));
        attributes.add(new Attribute("reserve", "Reservé", "boolean", true, null));
    }

    @GetMapping(path = "/dashboard/projections/{id}/tickets")
    public String showTickets(
            @PathVariable Long id,
            @RequestParam(name = "size", defaultValue = "12") int size,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ){
        Projection projection = projectionRepository.findById(id).get();
        Page<Ticket> ticketPage = ticketRepository.findTicketByProjection(projection, PageRequest.of(page, size));
        List<TicketPlace> ticketPlaces = new ArrayList<>();
        ticketPage.getContent().forEach(ticket -> ticketPlaces.add(new TicketPlace(ticket)));

        model.addAttribute("data", ticketPlaces);
        model.addAttribute("pages", new int[ticketPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", "tickets");
        model.addAttribute("parent", "projections");
        model.addAttribute("parentId", id);
        model.addAttribute("create", false);
        model.addAttribute("delete", false);
        model.addAttribute("parents", Stream.of(
                projection.getSalle().getCinema().getVille().getName(),
                projection.getSalle().getCinema().getName(),
                projection.getSalle().getName(),
                new SimpleDateFormat("yyyy-MM-dd").format(projection.getDate()) + " " +
                new SimpleDateFormat("HH:mm").format(projection.getSeance().getStartHour())
        ).toArray());
        model.addAttribute("backUrl", "/salles/" + projection.getSalle().getId() + "/projections");
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/tickets/{id}/edit")
    public String editTicket(@PathVariable Long id, Model model){
        Ticket ticket = ticketRepository.findById(id).get();
        formModel(model, "Modifier lr ticket", "edit", ticket, ticket.getProjection().getId());
        return "form";
    }

    @PostMapping(path = "/dashboard/projections/{id}/tickets/store")
    public String updateTicket(Ticket ticket, @PathVariable Long id){
        Ticket oldTicket = ticketRepository.findById(ticket.getId()).get();
        ticket.setPlace(oldTicket.getPlace());
        ticket.setProjection(oldTicket.getProjection());
        ticketRepository.save(ticket);
        return "redirect:/dashboard/projections/" + ticket.getProjection().getId() + "/tickets";
    }

    public void formModel(Model model, String title, String type, Ticket ticket, Long parentId){
        ticket.setId(new Long(0));
        model.addAttribute("data", ticket);
        model.addAttribute("title", title);
        model.addAttribute("type", type);
        model.addAttribute("name", "tickets");
        model.addAttribute("parent", "projections");
        model.addAttribute("parentId", parentId);
        model.addAttribute("attributes", attributes);
    }
}

@Data
class TicketPlace{
    private Long id;
    private double price;
    private String clientName;
    private Integer codePayment;
    private boolean reserve;
    private int placeNumber;

    TicketPlace(Ticket ticket){
        this.id = ticket.getId();
        this.price = ticket.getPrice();
        this.clientName = ticket.getClientName();
        this.codePayment = ticket.getCodePayment();
        this.reserve = ticket.isReserve();
        this.placeNumber = ticket.getPlace().getNumber();
    }
}

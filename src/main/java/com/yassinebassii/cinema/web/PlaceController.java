package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.*;
import com.yassinebassii.cinema.entities.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class PlaceController {

    @Autowired
    SalleRepository salleRepository;
    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    TicketRepository ticketRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public PlaceController(){
        attributes.add(new Attribute("id", "#", "number", false, null));
        attributes.add(new Attribute("number", "Number", "number", true, null));
        attributes.add(new Attribute("longitude", "Longitude", "number", true, null));
        attributes.add(new Attribute("latitude", "Latitude", "number", true, null));
        attributes.add(new Attribute("altitude", "Altitude", "number", true, null));
    }

    @GetMapping(path = "/dashboard/salles/{id}/places")
    public String showPlaces(
            @PathVariable Long id,
            @RequestParam(name = "size", defaultValue = "12") int size,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ){
        Salle salle = salleRepository.findById(id).get();
        Page<Place> placePage = placeRepository.findPlaceBySalle(salle, PageRequest.of(page, size));


        model.addAttribute("data", placePage.getContent());
        model.addAttribute("pages", new int[placePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", "places");
        model.addAttribute("parents", Stream.of(salle.getCinema().getVille().getName(), salle.getCinema().getName(), salle.getName())
                .toArray());
        model.addAttribute("parent", "salles");
        model.addAttribute("parentId", id);
        model.addAttribute("backUrl", "/cinemas/" + salle.getCinema().getId() + "/salles");
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/salles/{id}/places/create")
    public String createPlace(Model model, @PathVariable Long id){
        formModel(model, "Cr??er une place", "create", new Place(), id);
        return "form";
    }

    @GetMapping(path = "/dashboard/places/{id}/delete")
    public String deletePlace(@PathVariable Long id){
        Place place = placeRepository.findById(id).get();
        Salle salle = place.getSalle();
        placeRepository.delete(place);

        salle.setNbPlaces(salle.getNbPlaces() - 1);
        salleRepository.save(salle);
        return "redirect:/dashboard/salles/" + place.getSalle().getId() + "/places";
    }

    @GetMapping(path = "/dashboard/places/{id}/edit")
    public String editPlace(@PathVariable Long id, Model model){
        Place place = placeRepository.findById(id).get();
        formModel(model, "odifier la place", "edit", place, id);
        return "form";
    }

    @PostMapping(path = "/dashboard/salles/{id}/places/store")
    public String storePlace(Place place, @PathVariable Long id){
        Salle salle = salleRepository.findById(id).get();
        place.setSalle(salle);
        Long placeId = place.getId();
        placeRepository.save(place);
        if (placeId == null){
            salle.setNbPlaces(salle.getNbPlaces() + 1);
            salleRepository.save(salle);
            salle.getProjections().forEach(projection -> {
                Ticket ticket = new Ticket();
                ticket.setProjection(projection);
                ticket.setPlace(place);
                ticket.setReserve(false);
                ticketRepository.save(ticket);
            });
        }
        return "redirect:/dashboard/salles/" + place.getSalle().getId() + "/places";
    }

    public void formModel(Model model, String title, String type, Place place, Long parentId){
        model.addAttribute("data", place);
        model.addAttribute("title", title);
        model.addAttribute("type", type);
        model.addAttribute("name", "places");
        model.addAttribute("parent", "salles");
        model.addAttribute("parentId", parentId);
        model.addAttribute("attributes", attributes);
    }
}

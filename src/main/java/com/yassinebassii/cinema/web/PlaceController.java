package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.CinemaRepository;
import com.yassinebassii.cinema.dao.PlaceRepository;
import com.yassinebassii.cinema.dao.SalleRepository;
import com.yassinebassii.cinema.entities.Cinema;
import com.yassinebassii.cinema.entities.Place;
import com.yassinebassii.cinema.entities.Salle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class PlaceController {

    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    SalleRepository salleRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public PlaceController(){
        attributes.add(new Attribute("id", "#", "number", false, null));
        attributes.add(new Attribute("numero", "Nom", "text", true, null));
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
        model.addAttribute("child", "places");

        model.addAttribute("parents", Stream.of(
                salle.getCinema().getVille().getName(),
                salle.getCinema().getName(),
                salle.getName())
        .toArray());
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/places/create")
    public String storePlace(Model model){
        model.addAttribute("title", "Créer une place");
        model.addAttribute("type", "create");
        model.addAttribute("name", "places");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @GetMapping(path = "/dashboard/places/{id}/delete")
    public String deletePlace(@PathVariable Long id){
        Place place = placeRepository.findById(id).get();
        placeRepository.delete(place);
        return "redirect:/dashboard/salles/" + place.getSalle().getId() + "/places";
    }

    @GetMapping(path = "/dashboard/places/{id}/edit")
    public String editSalle(@PathVariable Long id, Model model){
        Place place = placeRepository.findById(id).get();
        model.addAttribute("data", place);
        model.addAttribute("title", "Modifier la Place");
        model.addAttribute("name", "places");
        model.addAttribute("type", "edit");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @PostMapping(path = "/dashboard/places/store")
    public String updateSalle(Place place){
        place = placeRepository.save(place);
        return "redirect:/dashboard/salles/" + place.getSalle().getId() + "/places";
    }
}

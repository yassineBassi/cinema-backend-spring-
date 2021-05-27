package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.CinemaRepository;
import com.yassinebassii.cinema.dao.VilleRepository;
import com.yassinebassii.cinema.entities.Cinema;
import com.yassinebassii.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class CinemaController {

    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    VilleRepository villeRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public CinemaController(){
        attributes.add(new Attribute("id", "#", "number", false, null));
        attributes.add(new Attribute("name", "Nom", "text", true, null));
        attributes.add(new Attribute("longitude", "Longitude", "number", true, null));
        attributes.add(new Attribute("latitude", "Latitude", "number", true, null));
        attributes.add(new Attribute("altitude", "Altitude", "number", true, null));
        attributes.add(new Attribute("nbSalles", "Les salles", "number", false, null));
    }

    @GetMapping(path = "/")
    public String index(){
        return "redirect:/dashboard";
    }

    @GetMapping(path = "/dashboard/villes/{id}/cinemas")
    public String showCinemas(
        @PathVariable Long id,
        @RequestParam(name = "size", defaultValue = "12") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        Model model
    ){
        Ville ville = villeRepository.findById(id).get();
        Page<Cinema> cinemaPage = cinemaRepository.findCinemasByVille(ville, PageRequest.of(page, size));
        model.addAttribute("data", cinemaPage.getContent());
        model.addAttribute("pages", new int[cinemaPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", "cinemas");
        model.addAttribute("child", "salles");
        model.addAttribute("parent", "villes");
        model.addAttribute("parentId", id);
        model.addAttribute("parents", Stream.of(ville.getName()).toArray());
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/villes/{id}/cinemas/create")
    public String createCinema(Model model, @PathVariable Long id){
        formModel(model, "Créer une Cinéma", "create", new Cinema(), id);
        return "form";
    }

    @GetMapping(path = "/dashboard/cinemas/{id}/delete")
    public String deleteCinema(@PathVariable Long id){
        Cinema cinema = cinemaRepository.findById(id).get();
        cinemaRepository.delete(cinema);
        return "redirect:/dashboard/villes/" + cinema.getVille().getId() + "/cinemas";
    }

    @GetMapping(path = "/dashboard/cinemas/{id}/edit")
    public String editCinema(@PathVariable Long id, Model model){
        Cinema cinema = cinemaRepository.findById(id).get();
        formModel(model, "Modifier la cinema", "edit", cinema, cinema.getVille().getId());
        return "form";
    }

    @PostMapping(path = "/dashboard/villes/{id}/cinemas/store")
    public String storeCinema(Cinema cinema, @PathVariable Long id){
        Ville ville = villeRepository.findById(id).get();
        cinema.setVille(ville);
        cinema = cinemaRepository.save(cinema);
        return "redirect:/dashboard/villes/" + cinema.getVille().getId() + "/cinemas";
    }

    public void formModel(Model model, String title, String type, Cinema cinema, Long parentId){
        model.addAttribute("data", cinema);
        model.addAttribute("title", title);
        model.addAttribute("type", type);
        model.addAttribute("name", "cinemas");
        model.addAttribute("parent", "villes");
        model.addAttribute("parentId", parentId);
        model.addAttribute("attributes", attributes);
    }
}

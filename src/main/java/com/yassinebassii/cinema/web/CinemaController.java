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

@Controller
public class CinemaController {

    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    VilleRepository villeRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public CinemaController(){
        attributes.add(new Attribute("id", "#", "number"));
        attributes.add(new Attribute("name", "Nom", "text"));
        attributes.add(new Attribute("longitude", "Longitude", "number"));
        attributes.add(new Attribute("latitude", "Latitude", "number"));
        attributes.add(new Attribute("altitude", "Altitude", "number"));
        attributes.add(new Attribute("nbSalles", "Les salles", "number"));
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
        model.addAttribute("path", ville.getName());
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/cinemas/create")
    public String storeCinema(Model model){
        model.addAttribute("title", "Créer une Ville");
        model.addAttribute("type", "create");
        model.addAttribute("name", "villes");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @GetMapping(path = "/dashboard/cinemas/{id}/delete")
    public String deleteCinema(@PathVariable Long id){
        cinemaRepository.deleteById(id);
        return "redirect:/dashboard/villes";
    }

    @GetMapping(path = "/dashboard/cinemas/{id}/edit")
    public String editVille(@PathVariable Long id, Model model){
        Cinema cinema = cinemaRepository.findById(id).get();
        model.addAttribute("data", cinema);
        model.addAttribute("title", "Modifier la cinema");
        model.addAttribute("name", "villes");
        model.addAttribute("type", "edit");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @PostMapping(path = "/dashboard/cinemas/store")
    public String updateVille(Cinema cinema){
        cinemaRepository.save(cinema);
        return "redirect:/dashboard/cinemas";
    }
}

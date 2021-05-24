package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.CinemaRepository;
import com.yassinebassii.cinema.dao.SalleRepository;
import com.yassinebassii.cinema.dao.VilleRepository;
import com.yassinebassii.cinema.entities.Cinema;
import com.yassinebassii.cinema.entities.Salle;
import com.yassinebassii.cinema.entities.Ville;
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
public class SalleController {

    @Autowired
    CinemaRepository cinemaRepository;
    @Autowired
    SalleRepository salleRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public SalleController(){
        attributes.add(new Attribute("id", "#", "number"));
        attributes.add(new Attribute("name", "Nom", "text"));
        attributes.add(new Attribute("nbPlaces", "Les places", "number"));
    }

    @GetMapping(path = "/dashboard/cinemas/{id}/salles")
    public String showSalles(
            @PathVariable Long id,
            @RequestParam(name = "size", defaultValue = "12") int size,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ){
        Cinema cinema = cinemaRepository.findById(id).get();
        Page<Salle> sallePage = salleRepository.findSalleByCinema(cinema, PageRequest.of(page, size));
        model.addAttribute("data", sallePage.getContent());
        model.addAttribute("pages", new int[sallePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", "salles");
        model.addAttribute("child", "projections");
        model.addAttribute("parents", Stream.of(
                cinema.getVille().getName(),
                cinema.getName())
        .toArray());
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/salles/create")
    public String storeSalle(Model model){
        model.addAttribute("title", "Créer une Salle");
        model.addAttribute("type", "create");
        model.addAttribute("name", "salles");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @GetMapping(path = "/dashboard/salles/{id}/delete")
    public String deleteSalle(@PathVariable Long id){
        Salle salle = salleRepository.findById(id).get();
        salleRepository.delete(salle);
        return "redirect:/dashboard/cinemas/" + salle.getCinema().getId() + "/salles";
    }

    @GetMapping(path = "/dashboard/salles/{id}/edit")
    public String editSalle(@PathVariable Long id, Model model){
        Salle salle = salleRepository.findById(id).get();
        model.addAttribute("data", salle);
        model.addAttribute("title", "Modifier la Salle");
        model.addAttribute("name", "salles");
        model.addAttribute("type", "edit");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @PostMapping(path = "/dashboard/salles/store")
    public String updateSalle(Salle salle){
        salle = salleRepository.save(salle);
        return "redirect:/dashboard/cinemas/" + salle.getCinema().getId() + "/salles";
    }
}

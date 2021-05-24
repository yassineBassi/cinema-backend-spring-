package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.ProjectionRepository;
import com.yassinebassii.cinema.dao.SalleRepository;
import com.yassinebassii.cinema.entities.Projection;
import com.yassinebassii.cinema.entities.Salle;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class ProjectionController {

    @Autowired
    ProjectionRepository projectionRepository;
    @Autowired
    SalleRepository salleRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public ProjectionController(){
        attributes.add(new Attribute("id", "#", "number"));
        attributes.add(new Attribute("filmPhoto", "", "photo"));
        attributes.add(new Attribute("date", "Nom", "date"));
        attributes.add(new Attribute("price", "Prix", "number"));
        attributes.add(new Attribute("filmTitle", "Film", "text"));
        attributes.add(new Attribute("filmDuration", "Durée", "number"));
        attributes.add(new Attribute("startHour", "début de séance", "date"));
    }

    @GetMapping(path = "/dashboard/salles/{id}/projections")
    public String showProjections(
            @PathVariable Long id,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ){
        Salle salle = salleRepository.findById(id).get();
        Page<Projection> projectionPage = projectionRepository.findProjectionBySalle(salle, PageRequest.of(page, size));
        List<ProjectionForm> projections = new ArrayList<>();
        for (Projection projection:projectionPage.getContent()){
           projections.add(new ProjectionForm(projection));
        }
        model.addAttribute("data", projections);
        model.addAttribute("pages", new int[projectionPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", "projections");
        model.addAttribute("child", "places");
        model.addAttribute("thumbnail", "filmPhoto");
        model.addAttribute("parents", Stream.of(salle.getCinema().getVille().getName(), salle.getCinema().getName(), salle.getName())
                                    .toArray());
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/projections/create")
    public String storeProjection(Model model){
        model.addAttribute("title", "Créer un projection");
        model.addAttribute("type", "create");
        model.addAttribute("name", "places");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @GetMapping(path = "/dashboard/projections/{id}/delete")
    public String deleteProjection(@PathVariable Long id){
        Projection projection = projectionRepository.findById(id).get();
        projectionRepository.delete(projection);
        return "redirect:/dashboard/salles/" + projection.getSalle().getId() + "/projections";
    }

    @GetMapping(path = "/dashboard/projections/{id}/edit")
    public String editProjection(@PathVariable Long id, Model model){
        Projection projection = projectionRepository.findById(id).get();
        model.addAttribute("data", projection);
        model.addAttribute("title", "Modifier la prjection");
        model.addAttribute("name", "projections");
        model.addAttribute("type", "edit");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @PostMapping(path = "/dashboard/projections/store")
    public String updateProjection(Projection projection){
        projection = projectionRepository.save(projection);
        return "redirect:/dashboard/salles/" + projection.getSalle().getId() + "/places";
    }
}

@Data
class ProjectionForm{
    private Long id;
    private Date date;
    private double price;
    private String filmTitle;
    private String filmPhoto;
    private double filmDuration;
    private String startHour;

    public ProjectionForm(Projection projection){
        this.id = projection.getId();
        this.date = projection.getDate();
        this.price = projection.getPrice();
        this.filmTitle = projection.getFilm().getTitre();
        this.filmPhoto = "films/" + projection.getFilm().getId() + "/image";
        this.filmDuration = projection.getFilm().getDuration();
        this.startHour = new SimpleDateFormat("HH:mm:ss").format(projection.getSeance().getStartHour());
    }
}

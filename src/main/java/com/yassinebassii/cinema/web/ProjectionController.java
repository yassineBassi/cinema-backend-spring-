package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.FilmRepository;
import com.yassinebassii.cinema.dao.ProjectionRepository;
import com.yassinebassii.cinema.dao.SalleRepository;
import com.yassinebassii.cinema.dao.SeanceRepository;
import com.yassinebassii.cinema.entities.Film;
import com.yassinebassii.cinema.entities.Projection;
import com.yassinebassii.cinema.entities.Salle;
import com.yassinebassii.cinema.entities.Seance;
import lombok.AllArgsConstructor;
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
public class ProjectionController {

    @Autowired
    ProjectionRepository projectionRepository;
    @Autowired
    SalleRepository salleRepository;
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    SeanceRepository seanceRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public ProjectionController(){
        attributes.add(new Attribute("id", "#", "number", false, null));
        attributes.add(new Attribute("filmPhoto", "", "photo", false, null));
        attributes.add(new Attribute("date", "Date", "date", true, null));
        attributes.add(new Attribute("price", "Prix", "number", true, null));
        attributes.add(new Attribute("filmTitle", "Film", "text", false, null));
        attributes.add(new Attribute("filmDuration", "Durée", "number", false, null));
        attributes.add(new Attribute("startHour", "début de séance", "date", false, null));
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
        model.addAttribute("child", "");
        model.addAttribute("thumbnail", "filmPhoto");
        model.addAttribute("parents", Stream.of(salle.getCinema().getVille().getName(), salle.getCinema().getName(), salle.getName())
                                    .toArray());
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/projections/create")
    public String createProjection(Model model){
        List<Film> films = filmRepository.findAll();
        List<ListOption> filmOptions = new ArrayList<>();
        films.forEach(film -> filmOptions.add(new ListOption(film.getId(), film.getTitle())));
        List<ListOption> seanceOptions = new ArrayList<>();
        List<Seance> seances = seanceRepository.findAll();
        seances.forEach(
            seance -> seanceOptions.add(
                new ListOption(
                    seance.getId(),
                    new SimpleDateFormat("HH:mm").format(seance.getStartHour())
                )
            )
        );

        attributes.add(new Attribute("film", "Film", "select", true, filmOptions));
        attributes.add(new Attribute("seance", "Séance", "select", true, seanceOptions));
        model.addAttribute("title", "Créer une projection");
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
    public String storeProjection(Projection projection){
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
        this.filmTitle = projection.getFilm().getTitle();
        this.filmPhoto = "films/" + projection.getFilm().getId() + "/image";
        this.filmDuration = projection.getFilm().getDuration();
        this.startHour = new SimpleDateFormat("HH:mm:ss").format(projection.getSeance().getStartHour());
    }
}


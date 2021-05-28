package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.*;
import com.yassinebassii.cinema.entities.*;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class FilmController {

    @Autowired
    FilmRepository filmRepository;
    @Autowired
    CategorieRepository categorieRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public FilmController(){
        attributes.add(new Attribute("id", "#", "number", false, null));
        attributes.add(new Attribute("photo", "Photo", "file", true, null));
        attributes.add(new Attribute("title", "Titre", "text", true, null));
        attributes.add(new Attribute("description", "Description", "textarea", true, null));
        attributes.add(new Attribute("director", "réalisateur", "text", true, null));
        attributes.add(new Attribute("releaseDate", "date de sortie", "date", true, null));
        attributes.add(new Attribute("duration", "Durée", "number", true, null));
    }

    @GetMapping(path = "/dashboard/films")
    public String showCinemas(
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ){
        Page<Film> filmPage = filmRepository.findAll(PageRequest.of(page, size));
        List<Film> films = filmPage.getContent();
        films.forEach(film -> film.setPhoto("films/" + film.getId() + "/image"));
        model.addAttribute("data", filmPage.getContent());
        model.addAttribute("pages", new int[filmPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", "films");
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    private List<ListOption> categorieOptions(){
        List<Categorie> categories = categorieRepository.findAll();
        List<ListOption> categorieOptions = new ArrayList<>();
        categories.forEach(categorie -> categorieOptions.add(new ListOption(categorie.getId(), categorie.getName())));
        return categorieOptions;
    }

    @GetMapping(path = "/dashboard/films/create")
    public String createFilm(Model model){
        formModel(model, "Créer un film", "create", new Film());
        return "form";
    }

    @GetMapping(path = "/dashboard/films/{id}/delete")
    public String deleteCinema(@PathVariable Long id){
        filmRepository.deleteById(id);
        return "redirect:/dashboard/films";
    }

    @GetMapping(path = "/dashboard/films/{id}/edit")
    public String editCinema(@PathVariable Long id, Model model){
        Film film = filmRepository.findById(id).get();
        film.setPhoto("films/" + film.getId() + "/image");
        formModel(model, "Modifier le film", "edit", film);
        return "form";
    }

    @PostMapping(path = "/dashboard/films/store")
    public String storeFilm(Film film, @RequestParam("photo-file") MultipartFile image, @RequestParam("categorieId") Long categorieId) throws IOException {
        Categorie categorie = categorieRepository.findById(categorieId).get();
        film.setCategorie(categorie);

        if (image.getOriginalFilename().length() != 0){
            String imageName = "film-" + film.getId() + "." + FilenameUtils.getExtension(image.getOriginalFilename());
            film.setPhoto(imageName);
            InputStream inputStream = image.getInputStream();
            Path dirPath = Paths.get(System.getProperty("user.home") + "/cinema/images");
            Path filePath = dirPath.resolve(imageName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }else if(film.getId() != 0){
            film.setPhoto(filmRepository.findById(film.getId()).get().getPhoto());
        }else{
            film.setPhoto("");
        }

        filmRepository.save(film);
        return "redirect:/dashboard/films";
    }

    public void formModel(Model model, String title, String type, Film film){
        List<Attribute> attributesCopy = new ArrayList<>();
        attributesCopy.addAll(attributes);
        attributesCopy.add(new Attribute("categorie", "Categorie", "select", true, categorieOptions()));
        System.out.println(attributesCopy);
        model.addAttribute("data", film);
        model.addAttribute("title", title);
        model.addAttribute("type", type);
        model.addAttribute("name", "films");
        model.addAttribute("attributes", attributesCopy);
    }
}

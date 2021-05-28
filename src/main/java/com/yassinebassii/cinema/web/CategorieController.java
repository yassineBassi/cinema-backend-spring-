package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.CategorieRepository;
import com.yassinebassii.cinema.dao.VilleRepository;
import com.yassinebassii.cinema.entities.Categorie;
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

@Controller
public class CategorieController {

    @Autowired
    private CategorieRepository categorieRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public CategorieController(){
        attributes.add(new Attribute("id", "#", "number", false, null));
        attributes.add(new Attribute("name", "Nom", "text", true, null));
    }

    @GetMapping(path = "/dashboard/categories")
    public String showCategories(
            @RequestParam(name = "size", defaultValue = "12") int size,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ){
        Page<Categorie> categoriePage = categorieRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("data", categoriePage.getContent());
        model.addAttribute("pages", new int[categoriePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", "categories");
        model.addAttribute("child", "films");
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/categories/create")
    public String storeVille(Model model){
        model.addAttribute("title", "Créer une Categorie");
        model.addAttribute("type", "create");
        model.addAttribute("name", "categories");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @GetMapping(path = "/dashboard/categories/{id}/delete")
    public String deleteCategorie(@PathVariable Long id){
        categorieRepository.deleteById(id);
        return "redirect:/dashboard/categories";
    }

    @GetMapping(path = "/dashboard/categories/{id}/edit")
    public String editCategorie(@PathVariable Long id, Model model){
        Categorie categorie = categorieRepository.findById(id).get();
        model.addAttribute("data", categorie);
        model.addAttribute("title", "Modifier la categorie");
        model.addAttribute("name", "categories");
        model.addAttribute("type", "edit");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @PostMapping(path = "/dashboard/categories/store")
    public String updateCategorie(Categorie categorie){
        categorieRepository.save(categorie);
        return "redirect:/dashboard/categories";
    }
}

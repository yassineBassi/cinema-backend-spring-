package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.VilleRepository;
import com.yassinebassii.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VilleController {

    @Autowired
    private VilleRepository villeRepository;

    @GetMapping(path = "/dashboard/villes")
    public String showVilles(Model model){
        String[] headers = {"id", "name"};
        List<Ville> villes = villeRepository.findAll();
        model.addAttribute("data", villes);
        model.addAttribute("name", "Villes");
        model.addAttribute("headers", headers);
        return "dashboard";
    }

    @PostMapping(path = "/dashboard/villes")
    public String storeVille(){
        return "";
    }

    @GetMapping(path = "/dashboard/villes/{id}/delete")
    public String deleteVille(@PathVariable Long id){
        villeRepository.deleteById(id);
        return "redirect:/dashboard/villes";
    }

    @PutMapping(path = "/dashboard/villes/{id}")
    public String updateVille(@PathVariable String id){
        return "";
    }
}

package com.yassinebassii.cinema.web;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.yassinebassii.cinema.dao.VilleRepository;
import com.yassinebassii.cinema.entities.Ville;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VilleController {

    @Autowired
    private VilleRepository villeRepository;

    private List<Attribute> attributes = new ArrayList<>();

    public VilleController(){
        attributes.add(new Attribute("id", "#", "number"));
        attributes.add(new Attribute("name", "Nom", "text"));
        attributes.add(new Attribute("longitude", "Longitude", "number"));
        attributes.add(new Attribute("latitude", "Latitude", "number"));
        attributes.add(new Attribute("altitude", "Altitude", "number"));
    }

    @GetMapping(path = "/dashboard/villes")
    public String showVilles(Model model){
        List<Ville> villes = villeRepository.findAll();
        model.addAttribute("data", villes);
        model.addAttribute("name", "villes");
        model.addAttribute("child", "cinemas");
        model.addAttribute("attributes", attributes);
        return "dashboard";
    }

    @GetMapping(path = "/dashboard/villes/create")
    public String storeVille(Model model){
        model.addAttribute("title", "Créer une Ville");
        model.addAttribute("type", "create");
        model.addAttribute("name", "villes");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @GetMapping(path = "/dashboard/villes/{id}/delete")
    public String deleteVille(@PathVariable Long id){
        villeRepository.deleteById(id);
        return "redirect:/dashboard/villes";
    }

    @GetMapping(path = "/dashboard/villes/{id}/edit")
    public String editVille(@PathVariable Long id, Model model){
        Ville ville = villeRepository.findById(id).get();
        model.addAttribute("data", ville);
        model.addAttribute("title", "Modifier la Ville");
        model.addAttribute("name", "villes");
        model.addAttribute("type", "edit");
        model.addAttribute("attributes", attributes);
        return "form";
    }

    @PostMapping(path = "/dashboard/villes/store")
    public String updateVille(Ville ville){
        villeRepository.save(ville);
        return "redirect:/dashboard/villes";
    }
}


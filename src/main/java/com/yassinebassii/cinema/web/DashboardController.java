package com.yassinebassii.cinema.web;

import com.yassinebassii.cinema.dao.VilleRepository;
import com.yassinebassii.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Controller
public class DashboardController{

    @Autowired
    private VilleRepository villeRepository;


    @GetMapping(path = "/dashboard")
    public String home(Authentication authentication) throws IOException {
        AtomicBoolean isAdmin = new AtomicBoolean(false);
        authentication.getAuthorities().forEach(grantedAuthority -> {
            if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                isAdmin.set(true);
            }
        });
        if(isAdmin.get()) return "redirect:dashboard/villes";
        return "redirect:login?error";
    }


}

package com.yassinebassii.cinema.dao;

import com.yassinebassii.cinema.entities.Categorie;
import com.yassinebassii.cinema.entities.Film;
import com.yassinebassii.cinema.entities.Salle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin("*")
public interface FilmRepository extends JpaRepository<Film, Long> {
    Page<Film> findFilmByCategorie(Categorie categorie, Pageable pageable);
}

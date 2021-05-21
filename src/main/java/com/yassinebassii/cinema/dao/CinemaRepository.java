package com.yassinebassii.cinema.dao;

import com.yassinebassii.cinema.entities.Cinema;
import com.yassinebassii.cinema.entities.Ville;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource
@CrossOrigin("*")
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Page<Cinema> findCinemasByVille(Ville ville, Pageable pageable
    );
}

package com.yassinebassii.cinema.dao;

import com.yassinebassii.cinema.entities.Place;
import com.yassinebassii.cinema.entities.Salle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin("*")
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Page<Place> findPlaceBySalle(Salle salle, Pageable pageable);
}

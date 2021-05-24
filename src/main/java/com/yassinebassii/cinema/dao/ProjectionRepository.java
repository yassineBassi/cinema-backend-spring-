package com.yassinebassii.cinema.dao;

import com.yassinebassii.cinema.entities.Projection;
import com.yassinebassii.cinema.entities.Salle;
import com.yassinebassii.cinema.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin("*")
public interface ProjectionRepository extends JpaRepository<Projection, Long> {
    Page<Projection> findProjectionBySalle(Salle salle, Pageable pageable);
}

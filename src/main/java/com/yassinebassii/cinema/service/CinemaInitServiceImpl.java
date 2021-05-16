package com.yassinebassii.cinema.service;

import com.yassinebassii.cinema.dao.*;
import com.yassinebassii.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService{

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void initUsers() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(null, "ADMIN"));
        roles.add(new Role(null, "USER"));
        roleRepository.saveAll(roles);

        userRepository.save(
            new User(
                new Long(1),
                "yassine",
                "bassi",
                "admin@admin.com",
                "$2a$10$lFsq0QkzIg4xBhM.44H8geN6.kgIAu1HCiBHz20xEkDtRqxK0WWbu",
                true,
                roles
            ));
    }

    @Override
    public void initVilles() {
        Stream.of("Agadir", "Casablanca", "Marrakech", "Tanger", "Rabat")
                .forEach(v -> {
                    Ville ville = new Ville();
                    ville.setName(v);
                    villeRepository.save(ville);
                });
    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(v -> {
            Stream.of("MegaRama", "IMAX", "FOUNOUN", "CHAHRAZAD", "DAOULIZE").forEach(cinemaName ->{
                Cinema cinema = new Cinema();
                cinema.setName(cinemaName);
                cinema.setVille(v);
                cinemaRepository.save(cinema);
            });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema -> {
            for (int i=0; i<new Random().nextInt(7 - 3) + 3; i++){
                Salle salle = new Salle();
                salle.setCinema(cinema);
                salle.setName("Salle " + (i + 1));
                salleRepository.save(salle);
            }
        });
    }

    @Override
    public void initSeances() {
        Stream.of("10:00", "12:00", "14:30", "16:15", "18:00", "21:00").forEach(sc -> {
            Seance seance = new Seance();
            try {
                seance.setHeurDebut(new SimpleDateFormat("HH:mm").parse(sc));
                seanceRepository.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for (int i=0; i < new Random().nextInt(30 - 15) + 15; i++){
                Place place = new Place();
                place.setNumero(i+1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });
    }

    @Override
    public void initCategories() {
        Stream.of("Histoire", "Action", "Fiction", "Drama").forEach(cat -> {
            Categorie categorie = new Categorie();
            categorie.setName(cat);
            categorieRepository.save(categorie);
        });
    }

    @Override
    public void initFilms() {
        double[] durees = {1, 1.5, 2, 2.5, 3, 3.5, 4};
        List<Categorie> categories = categorieRepository.findAll();
        Stream.of("Ip Man 4", "Outside the wire", "Chemical Hearts", "Underground", "Without Remorse")
        .forEach(flm -> {
            Film film = new Film();
            film.setTitre(flm);
            film.setCategorie(categories.get(new Random().nextInt(categories.size())));
            film.setDuree(durees[new Random().nextInt(durees.length)]);
            film.setPhoto(flm.replaceAll(" ", ""));
            filmRepository.save(film);
        });
    }

    @Override
    public void initProjections() {
        double[] prix = {30, 50, 60, 90, 70, 100};
        List<Film> films = filmRepository.findAll();
        villeRepository.findAll().forEach(ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    seanceRepository.findAll().forEach(seance -> {
                        Projection projection = new Projection();
                        projection.setSalle(salle);
                        projection.setFilm(films.get(new Random().nextInt(films.size())));
                        projection.setSeance(seance);
                        projection.setDate(new Date());
                        projection.setPrix(prix[new Random().nextInt(prix.length)]);
                        projectionRepository.save(projection);
                    });
                });
            });
        });
    }

    @Override
    public void initTickets() {
        projectionRepository.findAll().forEach((projection) -> {
            projection.getSalle().getPlaces().forEach(place -> {
                Ticket ticket = new Ticket();
                ticket.setPlace(place);
                ticket.setProjection(projection);
                ticket.setPrix(projection.getPrix());
                ticket.setReserve(false);
                ticketRepository.save(ticket);
            });
        });
    }
}

package dk.cphbusiness.moviedb_solution.persistence.daos;

import dk.cphbusiness.moviedb_solution.persistence.entities.Actor;
import dk.cphbusiness.moviedb_solution.persistence.entities.Genre;
import dk.cphbusiness.moviedb_solution.persistence.entities.Movie;
import dk.cphbusiness.moviedb_solution.persistence.entities.MovieEmployee;
import dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class MovieDAO implements IDAO<MovieDTO> {
    private static EntityManagerFactory emf;
    private static MovieDAO instance;

    private MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static MovieDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new MovieDAO(emf);
        }
        return instance;
    }

    @Override
    public MovieDTO create(MovieDTO movieDto) {
        Movie movie = movieDto.toEntity();
        try (EntityManager em = emf.createEntityManager()) {
            if(em.find(Movie.class, movie.getId()) != null){
                throw new EntityExistsException("Movie with id: " + movie.getId() + " already exists");
            }
            em.getTransaction().begin();
            // Actors
            Set<Actor> updatedActors = new HashSet<>();
            movie.getActors().forEach(actor -> {
                Actor foundActor = em.find(actor.getClass(), actor.getId());
                if (foundActor == null) {
                    actor = em.merge(actor);
                    updatedActors.add(actor);
                } else {
                    actor = foundActor;
                }
                if (!actor.getMovies().contains(movie)) {
                    actor.getMovies().add(movie);
                }
            });
            movie.setActors(updatedActors);
            // Movie Employees
            movie.getEmployees().forEach(employee -> {
                MovieEmployee foundEmp = em.find(employee.getClass(), employee.getId());
                if (foundEmp == null) {
                    em.persist(employee);
                } else {
                    employee = foundEmp;
                }
                employee.getMovies().add(movie);
            });
            // Genres

            movie.getGenres().forEach(genre -> {
                try {
                    Genre foundGenre = em.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                            .setParameter("name", genre.getName())
                            .getSingleResult();
                    genre = foundGenre;
                } catch (NoResultException e) {
                    em.persist(genre); // Persist the genre if it doesn't exist
                }
                if(!genre.getMovies().contains(movie)){
                    genre.getMovies().add(movie);
                }
            });
            em.persist(movie);
            em.getTransaction().commit();
            return new MovieDTO(movie);
        }
    }

    @Override
    public MovieDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return new MovieDTO(em.find(Movie.class, id));
        }
    }

    @Override
    public Set<MovieDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return Set.copyOf(em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m", MovieDTO.class).getResultList());
        }
    }

    @Override
    public MovieDTO update(MovieDTO movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie foundMovie = em.find(Movie.class, movie.getId());
            if (foundMovie == null) {
                throw new EntityNotFoundException("Can't find movie with id: " + movie.getId());
            }
            // update primitive fields
            foundMovie.setTitle(movie.getTitle());
            foundMovie.setDurationInMins(movie.getDurationInMins());
            foundMovie.setOriginCountry(movie.getOriginCountry().length > 0 ? movie.getOriginCountry()[0] : "");
            foundMovie.setReleaseDate(movie.getReleaseDate());
            foundMovie.setRevenue(movie.getRevenue());
            em.getTransaction().commit();
            return new MovieDTO(foundMovie);
        }
    }

    @Override
    public boolean delete(MovieDTO movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie m = em.find(Movie.class, movie.getId());
            if (m == null) {
                throw new EntityNotFoundException("Can't find movie with id: " + movie.getId());
            }
            em.remove(m);
            em.getTransaction().commit();
            return true;
        }
    }
}

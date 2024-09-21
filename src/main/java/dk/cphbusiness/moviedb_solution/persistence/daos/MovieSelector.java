package dk.cphbusiness.moviedb_solution.persistence.daos;

import dk.cphbusiness.moviedb_solution.persistence.entities.Actor;
import dk.cphbusiness.moviedb_solution.persistence.entities.MovieEmployee;
import dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class MovieSelector implements IMovieSelector {
    private static MovieSelector instance;
    private static EntityManagerFactory emf;

    private MovieSelector() {
    }

    public static MovieSelector getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new MovieSelector();
            emf = _emf;
        }
        return instance;
    }

    @Override
    public Set<MovieDTO> getAllMovies() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT m FROM Movie m", MovieDTO.class);
            return query.getResultStream().collect(Collectors.toSet());
        }
    }

    @Override
    public Set<MovieDTO> getMoviesWithRating() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m WHERE m.ratingCount > 0", MovieDTO.class);
            return query.getResultStream().collect(Collectors.toSet());
        }
    }

    @Override
    public Set<MovieDTO> getLowesRated() {
        Set<MovieDTO> allMovies = getMoviesWithRating();
        return allMovies
                .stream()
                .sorted((m1, m2) -> m1.getRating() - m2.getRating() > 0 ? 1 : m1.getRating() -m2.getRating() == 0? 0:-1)
                .limit(10)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<MovieDTO> getHighestRated() {
        Set<MovieDTO> allMovies = getMoviesWithRating();
        return allMovies.stream()
                .sorted((m1, m2) -> m2.getRating() - m1.getRating() > 0 ? 1 : m2.getRating() - m1.getRating() == 0? 0:-1)
                .limit(10)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<MovieDTO> getMostPopular() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m ORDER BY m.popularity", MovieDTO.class);
            return query.getResultStream().limit(10).collect(Collectors.toSet());
        }
    }

    @Override
    public double getAvgRating() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Double> query = em.createQuery("SELECT AVG(m.rating) FROM Movie m", Double.class);
            return query.getSingleResult();
        }

    }

    @Override
    public MovieDTO getMovieWithImdbId(String id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m WHERE m.imdbId = :imdbId", MovieDTO.class);
            query.setParameter("imdbId", id);
            return query.getSingleResult();
        }
    }

    @Override
    public Set<MovieDTO> getMoviesWithRevenue() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m WHERE m.revenue > 0", MovieDTO.class);
            return query.getResultStream().collect(Collectors.toSet());
        }
    }

    @Override
    public Set<MovieDTO> getMoviesWithActors() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m WHERE SIZE(m.actors) > 0", MovieDTO.class);
            return query.getResultStream().collect(Collectors.toSet());
        }
    }

    @Override
    public Set<MovieDTO> getMoviesByGenre(String genre) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m JOIN m.genres g WHERE g.name = :genreName", MovieDTO.class);
            query.setParameter("genreName",genre);
            return query.getResultStream().collect(Collectors.toSet());
        }
    }

    @Override
    public Set<MovieDTO> getMoviesByTitle(String title) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m WHERE m.title = :title", MovieDTO.class);
            query.setParameter("title",title);
            return query.getResultStream().collect(Collectors.toSet());
        }
    }

    @Override
    public Set<MovieDTO> getMoviesByActor(String actorName) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m JOIN m.actors a WHERE a.name = :actor", MovieDTO.class);
            query.setParameter("actor",actorName);
            return query.getResultStream().collect(Collectors.toSet());
        }
    }

    @Override
    public Set<MovieDTO> getMoviesByDirector(String director) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m JOIN m.employees e WHERE e.job = 'Director' AND e.name = :directorName", MovieDTO.class);
            query.setParameter("directorName",director);
            return query.getResultStream().collect(Collectors.toSet());
        }
    }

    @Override
    public Set<MovieEmployee> getEmployeesByJob(String job) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MovieDTO> query = em.createQuery("SELECT new dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO(m) FROM Movie m JOIN m.employees e WHERE e.job = :job ORDER BY ", MovieDTO.class);
            query.setParameter("job",job);
//            return query.getResultStream().collect(Collectors.toSet());
            return null;
        }
    }
}

package dk.cphbusiness.moviedb_solution;

import dk.cphbusiness.moviedb_solution.persistence.HibernateConfig;
import dk.cphbusiness.moviedb_solution.persistence.daos.IDAO;
import dk.cphbusiness.moviedb_solution.persistence.daos.MovieDAO;
import dk.cphbusiness.moviedb_solution.persistence.entities.Movie;
import dk.cphbusiness.moviedb_solution.service.IMovieReader;
import dk.cphbusiness.moviedb_solution.service.MovieReader;
import dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO;
import jakarta.persistence.EntityExistsException;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class Main {

    public static void main(String[] args) {
        IMovieReader instance = new MovieReader();
        IDAO<MovieDTO> movieDAO = MovieDAO.getInstance(HibernateConfig.getEntityManagerFactory());

        System.out.println("--------------------------------- Danish movies from past 5 years");
        LocalDate today = LocalDate.now();
        LocalDate yearsAgo5 = today.minusYears(5);
        Set<MovieDTO> movieDTOs = instance.getMoviesBasedOnCountryAndPeriod("DK", yearsAgo5, today);

        System.out.println("--------------------------------- Decorated Movies ---------------------------------");

        Set<MovieDTO> decoratedMovies = instance.decorateMoviesConcurrently(movieDTOs);
        System.out.println("Decorated movies size: " + decoratedMovies.size());
        decoratedMovies.forEach(movie -> movieDAO.create(movie));
    }
}

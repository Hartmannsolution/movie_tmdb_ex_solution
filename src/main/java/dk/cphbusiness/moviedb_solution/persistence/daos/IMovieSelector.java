package dk.cphbusiness.moviedb_solution.persistence.daos;

import dk.cphbusiness.moviedb_solution.persistence.entities.Actor;
import dk.cphbusiness.moviedb_solution.persistence.entities.MovieEmployee;
import dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO;

import java.util.Set;

public interface IMovieSelector {
    Set<MovieDTO> getAllMovies();
    Set<MovieDTO> getMoviesWithRating();
    Set<MovieDTO> getLowesRated();
    Set<MovieDTO> getHighestRated();
    Set<MovieDTO> getMostPopular();
   double getAvgRating();
    MovieDTO getMovieWithImdbId(String id);
    Set<MovieDTO> getMoviesWithRevenue();
    Set<MovieDTO> getMoviesWithActors();
    Set<MovieDTO> getMoviesByGenre(String genre);
    Set<MovieDTO> getMoviesByTitle(String title);
    Set<MovieDTO> getMoviesByActor(String actorName);
    Set<MovieDTO> getMoviesByDirector(String director);
    Set<MovieEmployee> getEmployeesByJob(String job);

}

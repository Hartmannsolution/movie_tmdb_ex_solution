package dk.cphbusiness.moviedb_solution.service;

import dk.cphbusiness.moviedb_solution.service.dtos.ActorDTO;
import dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO;
import dk.cphbusiness.moviedb_solution.service.dtos.MovieEmployeeDTO;

import java.time.LocalDate;
import java.util.*;

public interface IMovieReader {

    Map<Integer, String> getGenres();

    MovieDTO getMovieDataById(String id);

    Set<MovieDTO> getMoviesByTitle(String title);

    Set<MovieDTO> getMoviesMostPopular();
    // Comment

    Set<ActorDTO> getCastByMovieId(String id);

    MovieDTO getAllEmpByMovie(MovieDTO movie);

    Set<MovieEmployeeDTO> getCrewByMovieAndRole(String id, String role);

    Set<String> getRolesByMovieId(String id);

    Set<MovieDTO> getMoviesBasedOnCountryAndPeriod(String country, LocalDate startDate, LocalDate endDate);

    Set<MovieDTO> decorateMoviesConcurrently(Set<MovieDTO> movies);
}

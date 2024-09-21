package dk.cphbusiness.moviedb_solution.service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dk.cphbusiness.moviedb_solution.persistence.entities.Actor;
import dk.cphbusiness.moviedb_solution.persistence.entities.Genre;
import dk.cphbusiness.moviedb_solution.persistence.entities.Movie;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    private int id;
    @JsonProperty("imdb_id")
    private String imdbId;
    @JsonProperty("origin_country")
    private String[] originCountry;
    @JsonProperty("original_language")
    private String language;
    @JsonProperty("original_title")
    private String title;
    @JsonProperty("overview")
    private String story;
    @JsonProperty("runtime")
    private int durationInMins;
    private int revenue;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    @JsonProperty("genre_ids")
    private int[] genreIds;
    @JsonProperty("vote_average")
    private double rating;
    @JsonProperty("vote_count")
    private int ratingCount;
    @JsonProperty("popularity")
    private double popularity;
    // Computed field
    private Set<String> genreStrings = new HashSet<>();
    private Set<GenreDTO> genres;
    private Set<ActorDTO> actors;
    private Set<MovieEmployeeDTO> employees;

    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.imdbId = movie.getImdbId();
        this.originCountry = new String[]{movie.getOriginCountry()};
        this.language = movie.getLanguage();
        this.title = movie.getTitle();
        this.story = movie.getStory();
        this.durationInMins = movie.getDurationInMins();
        this.revenue = movie.getRevenue();
        this.releaseDate = movie.getReleaseDate();
        this.genreIds = movie.getGenres().stream().mapToInt(g -> g.getId()).toArray();
        this.genreStrings = movie.getGenres().stream().map(g -> g.getName()).collect(Collectors.toSet());
        this.rating = movie.getRating();
        this.ratingCount = movie.getRatingCount();
        this.popularity = movie.getPopularity();
        this.genres = movie.getGenres().stream().map(genre -> new GenreDTO(genre)).collect(Collectors.toSet());
        this.actors = movie.getActors().stream().map(actor -> new ActorDTO(actor)).collect(Collectors.toSet());
        this.employees = movie.getEmployees().stream().map(employee -> new MovieEmployeeDTO(employee)).collect(Collectors.toSet());
    }

    public Movie toEntity() {
        Movie thisOne = new Movie(id, imdbId, originCountry[0], language, title, story, durationInMins, revenue, releaseDate, rating, ratingCount, popularity);
        thisOne.setGenres(genres.stream().map(genreDto -> new Genre(genreDto.id, genreDto.name)).collect(Collectors.toSet()));
        thisOne.setActors(actors.stream().map(actorDTO -> actorDTO.toEntity()).collect(Collectors.toSet()));
        thisOne.setEmployees(employees.stream().map(employeeDTO -> employeeDTO.toEntity()).collect(Collectors.toSet()));
        return thisOne;
    }
}
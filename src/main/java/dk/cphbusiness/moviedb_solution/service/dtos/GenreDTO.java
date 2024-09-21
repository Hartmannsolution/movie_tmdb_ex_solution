package dk.cphbusiness.moviedb_solution.service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dk.cphbusiness.moviedb_solution.persistence.entities.Genre;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
public class GenreDTO {
    int id;
    String name;
    Set<Integer> movieIds = new HashSet<>();

    public GenreDTO(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
        genre.getMovies().forEach(m -> movieIds.add(m.getId()));
    }
}

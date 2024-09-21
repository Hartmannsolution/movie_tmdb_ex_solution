package dk.cphbusiness.moviedb_solution.service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dk.cphbusiness.moviedb_solution.persistence.entities.Actor;
import dk.cphbusiness.moviedb_solution.persistence.entities.Gender;
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
public class ActorDTO {
    int id;
    String gender; // 1 = Female, 2 = Male
    String name;
    String character;
    Set<String> movieTitles = new HashSet<>();

    public ActorDTO(Actor actor) {
        this.id = actor.getId();
        this.gender = actor.getGender().toString();
        this.name = actor.getName();
        this.character = actor.getCharacter();
        actor.getMovies().forEach(m -> movieTitles.add(m.getTitle()));
    }

    public Actor toEntity() {
        Gender gender = this.gender
                .equals("1")? Gender.Female :
                this.gender.equals("2")? Gender.Male :
                        Gender.Unknown; // 0 = Unknown
        return new Actor(id, gender, name, character );
    }
}

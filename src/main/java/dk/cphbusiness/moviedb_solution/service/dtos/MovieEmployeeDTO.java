package dk.cphbusiness.moviedb_solution.service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dk.cphbusiness.moviedb_solution.persistence.entities.Gender;
import dk.cphbusiness.moviedb_solution.persistence.entities.MovieEmployee;
import lombok.*;
import org.hibernate.boot.model.IdGeneratorStrategyInterpreter;

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
public class MovieEmployeeDTO {
    String gender; // 1 = Female, 2 = Male
    int id;
    String name;
    String department;
    String job;
    Set<String> movieTitles = new HashSet<>();

    public MovieEmployeeDTO(MovieEmployee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.department = employee.getDepartment();
        this.job = employee.getJob();
        employee.getMovies().forEach(m -> movieTitles.add(m.getTitle()));
    }

    public MovieEmployee toEntity() {
        Gender gender = this.gender.equals("1")?Gender.Female:Gender.Male;
        return new MovieEmployee(id, gender, name, department, job);
    }

}

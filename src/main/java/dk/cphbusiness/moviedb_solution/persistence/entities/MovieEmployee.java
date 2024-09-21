package dk.cphbusiness.moviedb_solution.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MovieEmployee {
    @Id
    int id;
    Gender gender;
    String name;
    String department;
    String job;
    @ManyToMany(mappedBy = "employees")
    private Set<Movie> movies = new HashSet<>();

    public MovieEmployee(int id, Gender gender, String name, String department, String job) {
        this.id = id;
        this.gender = gender;
        this.name = name;
        this.department = department;
        this.job = job;
    }

    // Movie collection
    public void addMovie(Movie m){
        this.movies.add(m);
        if(!m.getEmployees().contains(this)) {
            m.addEmployee(this);
        }
    }
    public void removeMovie(Movie m){
        this.movies.remove(m);
        m.removeEmployee(this);
    }
    @Override
    public String toString() {
        return "MovieEmployee{" +
                "id=" + id +
                "gender=" + gender.toString() +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MovieEmployee){
            MovieEmployee other = (MovieEmployee) obj;
            return this.id == other.id;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return this.id;
    }
}

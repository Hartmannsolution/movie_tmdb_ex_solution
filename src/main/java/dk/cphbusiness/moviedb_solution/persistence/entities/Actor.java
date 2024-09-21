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
public class Actor {
    @Id
    int id;
    Gender gender;
    String name;
    String character;
    @ManyToMany(mappedBy = "actors")
    private Set<Movie> movies = new HashSet<>();
    public Actor(int id, Gender gender, String name, String character) {
        this.id = id;
        this.gender = gender;
        this.name = name;
        this.character = character;
    }

    // Movie collection
    public void addMovie(Movie m){
        if (!this.movies.contains(m)) {
            this.movies.add(m);  // Add movie to actor's set
            m.getActors().add(this);  // Add actor to movie's set, but no recursive call
        }
    }

    public void removeMovie(Movie m){
        if (this.movies.contains(m)) {
            this.movies.remove(m);  // Remove movie from actor's set
            m.getActors().remove(this);  // Remove actor from movie's set, but no recursive call
        }
    }


    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", gender=" + gender +
                ", name='" + name + '\'' +
                ", character='" + character + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Actor other = (Actor) obj;
        return this.id == other.id;
    }
    @Override
    public int hashCode() {
        return this.id;
    }
}

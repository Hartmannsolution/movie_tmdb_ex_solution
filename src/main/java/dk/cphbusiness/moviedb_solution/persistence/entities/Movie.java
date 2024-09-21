package dk.cphbusiness.moviedb_solution.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class Movie {
    @Id
    private int id;
    private String imdbId;
    private String originCountry;
    private String language;
    private String title;
    @Column(name="story", columnDefinition = "TEXT")
    private String story;
    private int durationInMins;
    private int revenue;
    private LocalDate releaseDate;
    private double rating;
    private int ratingCount;
    private double popularity;

    @ManyToMany
    @JoinTable(name = "movie_genre",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;
    @ManyToMany
    @JoinTable(name = "movie_actor",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors;
    @ManyToMany
    @JoinTable(name = "movie_employee",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<MovieEmployee> employees;

    public Movie(int id, String imdbId, String originCOuntry, String language, String title, String story, int mins, int revenue, LocalDate date, double rating, int ratingCount, double popularity){
        this.id = id;
        this.imdbId = imdbId;
        this.originCountry = originCOuntry;
        this.language = language;
        this.title = title;
        this.story = story;
        this.durationInMins = mins;
        this.revenue = revenue;
        this.releaseDate = date;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.popularity = popularity;
    }

    // Genre collection
    public void addGenre(Genre g){
        this.genres.add(g);
        if(!g.getMovies().contains(this)) {
            g.addMovie(this);
        }
    }
    public void removeGenre(Genre g){
        this.genres.remove(g);
        g.removeMovie(this);
    }
    // Actor collection
    public void addActor(Actor a){
        if (!this.actors.contains(a)) {
            this.actors.add(a);  // Add actor to movie's set
            a.getMovies().add(this);  // Add movie to actor's set, but no recursive call
        }
    }

    public void removeActor(Actor a){
        if (this.actors.contains(a)) {
            this.actors.remove(a);  // Remove actor from movie's set
            a.getMovies().remove(this);  // Remove movie from actor's set, but no recursive call
        }
    }

    // Employee collection
    public void addEmployee(MovieEmployee e){
        this.employees.add(e);
        if(!e.getMovies().contains(this)) {
            e.addMovie(this);
        }
    }
    public void removeEmployee(MovieEmployee e){
        this.employees.remove(e);
        e.removeMovie(this);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", imdbId='" + imdbId + '\'' +
                ", originCountry='" + originCountry + '\'' +
                ", language='" + language + '\'' +
                ", title='" + title + '\'' +
                ", story='" + story + '\'' +
                ", durationInMins=" + durationInMins +
                ", revenue=" + revenue +
                ", releaseDate=" + releaseDate +
                ", rating=" + rating +
                ", ratingCount=" + ratingCount +
                ", popularity=" + popularity +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Movie other = (Movie) obj;
        return this.id == other.id;
    }
    @Override
    public int hashCode() {
        return this.id;
    }
}
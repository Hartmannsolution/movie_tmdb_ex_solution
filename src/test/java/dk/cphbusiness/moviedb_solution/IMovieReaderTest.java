package dk.cphbusiness.moviedb_solution;

import dk.cphbusiness.moviedb_solution.service.IMovieReader;
import dk.cphbusiness.moviedb_solution.service.MovieReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IMovieReaderTest {
    IMovieReader instance = new MovieReader();

    @Test
    void getGenres() {
        instance.getGenres().forEach((k, v) -> System.out.println(k + " : " + v));
        String expected = "Documentary";
        String actual = instance.getGenres().get(99);
        assertEquals(expected, actual);
    }

    @Test
    void getMovieDataById() {
        String expected = "The Shawshank Redemption";
//        String actual = instance.getMovieDataById("tt0111161").getTitle();
        String actual = instance.getMovieDataById("tt0111161").getTitle();
        assertEquals(expected, actual);
    }

    @Test
    void getMoviesByTitle() {
        instance.getMoviesByTitle("Matrix").forEach(System.out::println);
        boolean actual = instance.getMoviesByTitle("Matrix").size() > 0;
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    void getMoviesMostPopular() {
        System.out.println(instance.getMoviesMostPopular().size());
        boolean actual = instance.getMoviesMostPopular().size() == 20;
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    void getCastByMovieId() {
        System.out.println(instance.getCastByMovieId("tt0111161").size());
        boolean actual = instance.getCastByMovieId("tt0111161").size() == 60;
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    void getCrewByMovieAndRole() {
        System.out.println(instance.getCrewByMovieAndRole("tt0111161", "Director").size());
        boolean actual = instance.getCrewByMovieAndRole("tt0111161", "director").size() == 8;
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    void getRolesByMovieId() {
        instance.getRolesByMovieId("tt0111161").forEach(System.out::println);
        System.out.println(instance.getRolesByMovieId("tt0111161").size());
        boolean actual = instance.getRolesByMovieId("tt0111161").size() == 111;
        boolean expected = true;
        assertEquals(expected, actual);
    }

//    @Test
//    @DisplayName("Get danish movies")
//    void getDanishMovies(){
//        System.out.println(instance.getDanishMovies().size());
//        boolean actual = instance.getDanishMovies().size() == 1;
//        boolean expected = true;
//        assertEquals(expected, actual);
//    }
}
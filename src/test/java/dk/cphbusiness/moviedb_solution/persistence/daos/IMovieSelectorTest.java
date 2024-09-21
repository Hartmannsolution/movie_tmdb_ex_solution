package dk.cphbusiness.moviedb_solution.persistence.daos;

import dk.cphbusiness.moviedb_solution.persistence.HibernateConfig;
import dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IMovieSelectorTest {
    private static IMovieSelector instance;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactory(); // Database is not for test, since all data is stored in dev db
        instance = MovieSelector.getInstance(emf);
    }

    @AfterAll
    static void tearDownAll() {
    }

    @BeforeEach
    void setUp() {
//        try(EntityManager em = emf.createEntityManager()){
//
//        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllMovies() {
        int actual = instance.getAllMovies().size();
        int expected = 1308;
        assertEquals(expected, actual);
    }

    @Test
    void getMoviesWithRating() {
        int actual = instance.getMoviesWithRating().size();
        int expected = 614;
        assertEquals(expected, actual);
    }

    @Test
    void getLowesRated() {
        Set<MovieDTO> lowesRated = instance.getLowesRated();
        lowesRated.forEach(System.out::println);
        int actual = lowesRated.size();
        int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    void getHighestRated() {
        Set<MovieDTO> highestRated = instance.getHighestRated();
        highestRated.forEach(System.out::println);
        int actual = highestRated.size();
        int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    void getMostPopular() {
        Set<MovieDTO> popular = instance.getMostPopular();
        popular.forEach(System.out::println);
        int actual = popular.size();
        int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    void getAvgRating(){
        double actual = instance.getAvgRating();
        double expected = 3.118;
        double epsilon = 0.001d; // floating point precision
        assertEquals(expected, actual, epsilon);
    }

    @Test
    void getMoviesWithImdbId() {
        MovieDTO movie = instance.getMovieWithImdbId("tt14419036");
        String actual = movie.getTitle();
        String expected = "Incognito";
        assertEquals(expected, actual);
    }

    @Test
    void getMoviesWithRevenue() {
        int size = instance.getMoviesWithRevenue().size();
        int expected = 63;
        assertEquals(expected, size);
    }

    @Test
    void getMoviesWithActors() {
        int size = instance.getMoviesWithActors().size();
        int expected = 793;
        assertEquals(expected, size);
    }

    @Test
    void getMoviesByGenre() {
        Set<MovieDTO> movies = instance.getMoviesByGenre("Drama");
        int size = movies.size();
        int expected = 350;
        System.out.println(movies);
        assertEquals(expected, size);
    }

    @Test
    void getMoviesByTitle() {
        Set<MovieDTO> movies = instance.getMoviesByTitle("De Ukuelige");
        int size = movies.size();
        int expected = 1;
        System.out.println(movies);
        assertEquals(expected, size);
    }

    @Test
    void getMoviesByActor() {
        Set<MovieDTO> movies = instance.getMoviesByActor("Sidse Babett Knudsen");
        int size = movies.size();
        int expected = 1;
        System.out.println(movies);
        assertEquals(expected, size);
    }

    @Test
    void getMoviesByDirector() {
        Set<MovieDTO> movies = instance.getMoviesByDirector("Søren Pilmark");
        int size = movies.size();
        int expected = 1;
        System.out.println(movies);
        assertEquals(expected, size);
    }
}
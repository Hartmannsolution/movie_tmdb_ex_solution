package dk.cphbusiness.moviedb_solution.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cphbusiness.moviedb_solution.Utils;
import dk.cphbusiness.moviedb_solution.persistence.HibernateConfig;
import dk.cphbusiness.moviedb_solution.persistence.daos.IDAO;
import dk.cphbusiness.moviedb_solution.persistence.daos.MovieDAO;
import dk.cphbusiness.moviedb_solution.service.dtos.ActorDTO;
import dk.cphbusiness.moviedb_solution.service.dtos.MovieDTO;
import dk.cphbusiness.moviedb_solution.service.dtos.MovieEmployeeDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class MovieReader implements IMovieReader {
    private final String GENRE_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=#";
    private final String MOVIE_ID_URL = "https://api.themoviedb.org/3/movie/$?api_key=#";
    private final String MOVIE_SEARCH_URL = "https://api.themoviedb.org/3/search/movie?query=$&api_key=#";
    private final String MOVIE_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=#";
    private final String MOVIE_CREDITS_URL = "https://api.themoviedb.org/3/movie/$/credits?api_key=#";
    private final String MOVIE_COUNTRY_PERIOD = "https://api.themoviedb.org/3/discover/movie?api_key=#&language=dk-DA&release_date.gte=€&release_date.lte=$&sort_by=original_title.asc&with_origin_country=§&page=π";
    private final String MOVIE_API_KEY = new Utils().getPropertyValue("movie.api.key");

    private final ObjectMapper om = new Utils().getObjectMapper();
    private final IDAO<MovieDTO> movieDAO = MovieDAO.getInstance(HibernateConfig.getEntityManagerFactory());

    public static void main(String[] args) {
        MovieReader instance = new MovieReader();
        // Get a map of the available genres from the API
        Map<Integer, String> genres = instance.getGenres();
//
//        genres.forEach((k, v) -> System.out.println(k + " : " + v));
//        Set<MovieDTO> movies = new MovieReader().getMoviesByTitle("Matrix");
//
//        Set<MovieDTO> popularMovies = new MovieReader().getMoviesMostPopular();
//        Set<MovieDTO> moviesWithGenres = instance.addGenresToMovies(movies);
//        moviesWithGenres.forEach(System.out::println);
//        System.out.println("--------------------------------- By ID ---------------------------------");
//        System.out.println("Movie Reader: " + new MovieReader().getMovieDataById("tt15428940"));
//        System.out.println("--------------------------------- Cast By ID: ---------------------------------");
//        new MovieReader().getCastByMovieId("tt15428940").forEach(System.out::println);
//        System.out.println("--------------------------------- Crew By ID: ---------------------------------");
//        new MovieReader().getCrewByMovieAndRole("tt15428940", "Director").forEach(System.out::println);
//        System.out.println("--------------------------------- Movies by title: ---------------------------------");
//        movies.forEach(System.out::println);
//        System.out.println("--------------------------------- Popular Movies: ---------------------------------");
//        popularMovies.forEach(System.out::println);
//        System.out.println("--------------------------------- Roles by movie ID: ---------------------------------");
//        new MovieReader().getRolesByMovieId("tt15428940").forEach(System.out::println);
//        System.out.println();
//        System.out.println("--------------------------------- Danish movies from past 5 years");
//        LocalDate today = LocalDate.now();
//        LocalDate yearsAgo5 = today.minusYears(5);
//////        Set<Movie> results = instance.getMoviesBasedOnCountryAndPeriod("DK", yearsAgo5, today);
//////        System.out.println("SIZE: "+results.size());
//////        System.out.println("First: "+ results.iterator().next());
//        Set<MovieDTO> movieDTOs = instance.getMoviesBasedOnCountryAndPeriod("DK", yearsAgo5, today);
//        System.out.println(movieDTOs);
//        System.out.println("--------------------------------- Decorated Movies ---------------------------------");
//        Set<MovieDTO> decoratedMovies = instance.decorateMoviesConcurrently(movieDTOs.stream().limit(5).collect(Collectors.toSet()));
//
//        System.out.println("Decorated movies size: " + decoratedMovies.size());
//        decoratedMovies.forEach(System.out::println);
//        MovieDTO toPersist = decoratedMovies.stream().filter(m->m.getActors().size()>2).collect(Collectors.toSet()).iterator().next();
//        try {
//            MovieDTO created = instance.movieDAO.create(toPersist);
//            System.out.println("TO PERSIST: " + toPersist);
//            System.out.println("CREATED: " + created);
//        } catch (EntityExistsException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public MovieDTO getMovieDataById(String id) { // Either TMDB id or IMDB id works

        try {
            String fullUrl = MOVIE_ID_URL
                    .replace("#", MOVIE_API_KEY)
                    .replace("$", id);

            // Using HttpClient which is many more code lines than using Jacksom Object Mapper
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body();
                System.out.println("BODY: " + body);
                MovieDTO movie = om.readValue(body, MovieDTO.class);
                return movie;
            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
                throw new RuntimeException("Failed request");
            }

        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, String> getGenres() {
        try {
            String fullUrl = GENRE_URL.replace("#", MOVIE_API_KEY);

            // Read the JSON data from the URL. The genres are in an array under the key "genres"
            JsonNode allData = om.readTree(new URL(fullUrl));
            Set<JsonNode> genres = om.convertValue(allData.get("genres"), new TypeReference<Set<JsonNode>>() {
            });

            // Convert JsonNodes to Map
            return genres.stream()
                    .map(genre -> Map.entry(genre.get("id"), genre.get("name")))
                    .collect(Collectors.toMap(entry -> entry.getKey().asInt(), entry -> entry.getValue().asText()));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<MovieDTO> getMoviesByTitle(String title) {
        try {
            String fullUrl = MOVIE_SEARCH_URL
                    .replace("#", MOVIE_API_KEY)
                    .replace("$", title);

            JsonNode allData = om.readTree(new URL(fullUrl));
//            Do it simply with an array or convert directly to set like below (https://www.baeldung.com/jackson-linkedhashmap-cannot-be-cast))
//            Movie[] movies = om.convertValue(allData.get("results"), Movie[].class);

            Set<MovieDTO> movies = om.convertValue(allData.get("results"), new TypeReference<Set<MovieDTO>>() {
            });
            movies = addGenresToMovies(movies);
            return movies;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<MovieDTO> getMoviesMostPopular() {
        try {
            String fullUrl = MOVIE_POPULAR_URL.replace("#", MOVIE_API_KEY);
            JsonNode allData = om.readTree(new URL(fullUrl));
            Set<MovieDTO> movies = om.convertValue(allData.get("results"), new TypeReference<Set<MovieDTO>>() {
            });
            return movies;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<ActorDTO> getCastByMovieId(String id) {
        try {
            String fullUrl = MOVIE_CREDITS_URL
                    .replace("#", MOVIE_API_KEY)
                    .replace("$", id);
            JsonNode allData = om.readTree(new URL(fullUrl));
            Set<ActorDTO> actors = om.convertValue(allData.get("cast"), new TypeReference<Set<ActorDTO>>() {
            });
            return actors;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public MovieDTO getAllEmpByMovie(MovieDTO movie) {
        try {
            String fullUrl = MOVIE_CREDITS_URL
                    .replace("#", MOVIE_API_KEY)
                    .replace("$", String.valueOf(movie.getId()));
            System.out.println(fullUrl);
            JsonNode allData = om.readTree(new URL(fullUrl));
            Set<ActorDTO> actors = om.convertValue(allData.get("cast"), new TypeReference<Set<ActorDTO>>() { });
            Set<MovieEmployeeDTO> employees = om.convertValue(allData.get("crew"), new TypeReference<Set<MovieEmployeeDTO>>() { });
            movie.setActors(actors);
            movie.setEmployees(employees);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return movie;
    }


    @Override
    public Set<MovieEmployeeDTO> getCrewByMovieAndRole(String id, String role) {
        try {
            String fullUrl = MOVIE_CREDITS_URL
                    .replace("#", MOVIE_API_KEY)
                    .replace("$", id);
            JsonNode allData = om.readTree(new URL(fullUrl));
            Set<MovieEmployeeDTO> crew = om.convertValue(allData.get("crew"), new TypeReference<Set<MovieEmployeeDTO>>() {
            });
            return crew.stream()
                    .filter(employee -> employee.getJob().toLowerCase().contains(role.toLowerCase()))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getRolesByMovieId(String id) {
        try {
            String fullUrl = MOVIE_CREDITS_URL
                    .replace("#", MOVIE_API_KEY)
                    .replace("$", id);
            JsonNode allData = om.readTree(new URL(fullUrl));
            Set<MovieEmployeeDTO> crew = om.convertValue(allData.get("crew"), new TypeReference<Set<MovieEmployeeDTO>>() {
            });
            return crew.stream()
                    .map(emp -> emp.getJob())
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<MovieDTO> getMoviesBasedOnCountryAndPeriod(String country, LocalDate startDate, LocalDate endDate) {
        Set<MovieDTO> movieCollection = new HashSet<>();
        try {
            String fullUrl = MOVIE_COUNTRY_PERIOD
                    .replace("#", MOVIE_API_KEY)
                    .replace("€", startDate.toString())
                    .replace("$", endDate.toString())
                    .replace("§", country);
            String page1Url = fullUrl.replace("π", "1");

            // Read the JSON data from the URL. The genres are in an array under the key "genres"
            JsonNode allData = om.readTree(new URL(page1Url));
            int totalPages = allData.get("total_pages").asInt();
            // Add all movies from the first page
            movieCollection.addAll(om.convertValue(allData.get("results"), new TypeReference<Set<MovieDTO>>() {
            }));
//            for(int i = 2; i <=totalPages; i++){
//                String pageUrl = fullUrl.replace("π", String.valueOf(i));
//                JsonNode pageData = om.readTree(new URL(pageUrl));
//                movieCollection.addAll(om.convertValue(pageData.get("results"), new TypeReference<Set<Movie>(){}));
//            }
            movieCollection.addAll(paginatedConcurrentCalls(fullUrl, totalPages));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return movieCollection;
    }


    @Override
    public Set<MovieDTO> decorateMoviesConcurrently(Set<MovieDTO> movies) {
        ExecutorService xService = Executors.newFixedThreadPool(20);
        CompletionService<MovieDTO> completionService = new ExecutorCompletionService<>(xService); // to simplify future handling and start processing results as soon as they are ready

        Set<MovieDTO> decoratedMovies = Collections.newSetFromMap(new ConcurrentHashMap<>());  // Thread-safe set

        // Submit all the tasks (they will be available in the order of completion inside the completion service)
        for (MovieDTO movie : movies) {
           completionService.submit(() -> getAllEmpByMovie(getMovieDataById(String.valueOf(movie.getId()))));
        }
        // Take the number of Futures as they are available
        for(int i = 0; i < movies.size(); i++){
            try {
                MovieDTO decoratedMovie = completionService.take().get();
                decoratedMovies.add(decoratedMovie);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // Shutdown executor service
        xService.shutdown();
        try {
            xService.awaitTermination(1, TimeUnit.MINUTES);  // Gracefully wait for all tasks to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return decoratedMovies;
    }

    private Set<MovieDTO> paginatedConcurrentCalls(String url, int numberOfPages) {
        ExecutorService xService = Executors.newFixedThreadPool(10);
        CompletionService<Set<MovieDTO>> completionService = new ExecutorCompletionService<>(xService); // to simplify future handling and start processing results as soon as they are ready
        Set<MovieDTO> movies = Collections.newSetFromMap(new ConcurrentHashMap<>());  // Thread-safe set

        List<Future<Set<MovieDTO>>> futures = new ArrayList<>();
        for (int i = 2; i <= numberOfPages; i++) {
            int pageIndex = i; // effectively final for lambda
            Future<Set<MovieDTO>> fut = xService.submit(() -> {
                String pageUrl = url.replace("π", String.valueOf(String.valueOf(pageIndex)));
                try {
                    JsonNode pageData = om.readTree(new URL(pageUrl));
                    return om.convertValue(pageData.get("results"), new TypeReference<Set<MovieDTO>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    return Collections.emptySet(); // if error occurs just continuoue with next page
                }
            });
            futures.add(fut);
        }
        for (Future<Set<MovieDTO>> movieFuture : futures) {
            try {
                movies.addAll(movieFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                throw new RuntimeException("Something went wrong with reading the future");
            }
        }
        // Shutdown executor service
        xService.shutdown();
        try {
            xService.awaitTermination(1, TimeUnit.MINUTES);  // Gracefully wait for all tasks to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private Set<MovieDTO> addGenresToMovies(Set<MovieDTO> movies) {
        Map<Integer, String> genres = getGenres();
        return movies.stream()
                .map(movie -> {
                    Arrays.stream(movie.getGenreIds())
                            .forEach(id -> movie.getGenreStrings().add(genres.get(id)));
                    return movie;
                }).collect(Collectors.toSet());
    }

    private Set<MovieDTO> addCreditsToMovies(Set<MovieDTO> movies) {
        return movies.stream().map(movie -> {
            movie.setActors(getCastByMovieId(movie.getImdbId()));
            movie.setEmployees(getCrewByMovieAndRole(movie.getImdbId(), "Director"));
            return movie;
        }).collect(Collectors.toSet());
    }

}

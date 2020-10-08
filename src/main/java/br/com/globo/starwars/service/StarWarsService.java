package br.com.globo.starwars.service;

import br.com.globo.starwars.model.Movie;
import br.com.globo.starwars.model.MovieWrapper;
import br.com.globo.starwars.model.People;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StarWarsService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "https://swapi.dev/api/";
    private static final String URL_MOVIES = BASE_URL + "films/";
    private static final String URL_MOVIE_BY_ID = URL_MOVIES + "{0}/";

    public List<Movie> getAllMovies() {
        return restTemplate.getForObject(URL_MOVIES, MovieWrapper.class).getMovies();
    }

    public Movie getMovieById(Long id) throws JsonProcessingException {
        Movie movie = restTemplate.getForObject(MessageFormat.format(URL_MOVIE_BY_ID, id), Movie.class);
        movie.setId(id);
        movie.setCast(createListCharactersForMovie(movie));

        return movie;
    }

    private List<String> createListCharactersForMovie(Movie movie) {
        List<String> cast = new ArrayList<>();

        if (movie.getCharacters() != null && !movie.getCharacters().isEmpty()) {
            for (String people : movie.getCharacters()) {
                String url = people.replaceAll("http", "https");
                ResponseEntity<People> response = restTemplate.exchange(url, HttpMethod.GET, null, People.class);

                if (response.getBody() != null) {
                    cast.add(response.getBody().getName());
                }
            }
        }
        return cast;
    }

}

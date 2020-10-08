package br.com.globo.starwars.service;

import br.com.globo.starwars.exception.ApiRequestException;
import br.com.globo.starwars.model.Movie;
import br.com.globo.starwars.model.MovieWrapper;
import br.com.globo.starwars.model.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private static final String BASE_URL = "https://swapi.dev/api/";
    private static final String URL_MOVIES = BASE_URL + "films/";
    private static final String URL_MOVIE_BY_ID = URL_MOVIES + "{0}/";

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable(cacheNames = "movies")
    public List<Movie> getAllMovies() {
        ResponseEntity<MovieWrapper> response = restTemplate.exchange(URL_MOVIES, HttpMethod.GET, null, MovieWrapper.class);

        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new ApiRequestException("Erro ao recuperar a lista de filmes");
        }

        List<Movie> movies = response.getBody().getMovies();

        for (Movie movie : movies) {
            movie.setCast(createListCharactersForMovie(movie));
        }

        return movies;
    }

    @Cacheable(cacheNames = "movie", key = "#id")
    public Movie getMovieById(Long id) {
        ResponseEntity<Movie> response = restTemplate.exchange(MessageFormat.format(URL_MOVIE_BY_ID, id), HttpMethod.GET, null, Movie.class);

        if (HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {
            throw new ApiRequestException("Nao foi encontrado filme com o id " + id);
        } else if (!HttpStatus.OK.equals(response.getStatusCode())) {
            throw new ApiRequestException("Erro ao recuperar o filme com o id informado");
        }

        Movie movie = response.getBody();
        movie.setId(id);
        movie.setCast(createListCharactersForMovie(movie));

        return movie;
    }

    public List<String> createListCharactersForMovie(Movie movie) {
        List<String> cast = new ArrayList<>();

        if (movie.getCharacters() != null && !movie.getCharacters().isEmpty()) {
            for (String characterUrl : movie.getCharacters()) {
                String url = characterUrl.replaceAll("http", "https");
                ResponseEntity<People> response = restTemplate.exchange(url, HttpMethod.GET, null, People.class);

                if (!HttpStatus.OK.equals(response.getStatusCode())) {
                    throw new ApiRequestException("Erro ao pesquisar o personagem da URL " + characterUrl);
                }

                if (response.getBody() != null) {
                    cast.add(response.getBody().getName());
                }
            }
        }
        return cast;
    }
}

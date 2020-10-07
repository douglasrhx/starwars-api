package br.com.globo.starwars.service;

import br.com.globo.starwars.model.Movie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collections;

@Service
public class StarWarsService {

    private static final String BASE_URL = "https://swapi.dev/api/";
    private static final String URL_MOVIES = BASE_URL + "films/";
    private static final String URL_MOVIE_BY_ID = URL_MOVIES + "{0}/";

    public Movie getMovieById(Long id) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(MessageFormat.format(URL_MOVIE_BY_ID, id), HttpMethod.GET, entity, Movie.class).getBody();
    }
}

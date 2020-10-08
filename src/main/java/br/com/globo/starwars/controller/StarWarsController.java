package br.com.globo.starwars.controller;

import br.com.globo.starwars.model.Movie;
import br.com.globo.starwars.service.StarWarsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("starwars")
public class StarWarsController {

    @Autowired
    private StarWarsService starWarsService;

    @GetMapping("movies/{id}")
    public Movie getMovieById(@PathVariable("id") Long id) throws JsonProcessingException {
        return starWarsService.getMovieById(id);
    }

    @GetMapping("movies")
    public List<Movie> getAllMovies() { return starWarsService.getAllMovies(); }
}

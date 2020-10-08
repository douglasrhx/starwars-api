package br.com.globo.starwars.controller;

import br.com.globo.starwars.model.Movie;
import br.com.globo.starwars.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("starwars")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("movies/{id}")
    public Movie getMovieById(@PathVariable("id") Long id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("movies")
    public List<Movie> getAllMovies() { return movieService.getAllMovies(); }
}

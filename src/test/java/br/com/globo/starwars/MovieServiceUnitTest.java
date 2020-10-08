package br.com.globo.starwars;

import br.com.globo.starwars.exception.ApiRequestException;
import br.com.globo.starwars.model.Movie;
import br.com.globo.starwars.model.MovieWrapper;
import br.com.globo.starwars.model.People;
import br.com.globo.starwars.service.MovieService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MovieServiceUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @Spy
    @InjectMocks
    private MovieService movieService;

    @Test
    public void testGetMovieWithId1ShouldReturnObject() {
        Long id = 1L;

        Movie movie = getMovieWithId1();

        mockRestTemplateExchange(movie, HttpStatus.OK);

        Mockito.doReturn(Collections.singletonList("http://swapi.dev/api/people/1")).when(movieService).createListCharactersForMovie(movie);

        Movie movieResponse = movieService.getMovieById(id);

        Assert.assertEquals(1, movieResponse.getCast().size());
        Assert.assertEquals(movie.getTitle(), movieResponse.getTitle());
    }

    @Test
    public void testGetMovieByIdMovieNotFoundShouldThrowException() {
        Long id = 99L;
        try {
            mockRestTemplateExchange(new Movie(), HttpStatus.NOT_FOUND);
            Movie movieResponse = movieService.getMovieById(id);
        } catch (ApiRequestException a) {
            Assert.assertEquals("Nao foi encontrado filme com o id " + id, a.getMessage());
        }
    }

    @Test
    public void testGetMovieByIdBadRequestShouldThrowException() {
        Long id = 99L;
        try {
            mockRestTemplateExchange(new Movie(), HttpStatus.BAD_REQUEST);
            Movie movieResponse = movieService.getMovieById(id);
        } catch (ApiRequestException a) {
            Assert.assertEquals("Erro ao recuperar o filme com o id informado", a.getMessage());
        }
    }

    @Test
    public void testCreateListCharactersForMovie() {
        Movie movie = getMovieWithId1();

        People people = new People();
        people.setName("Luke Skywalker");

        mockRestTemplateExchange(people, HttpStatus.OK);

        movie.setCast(movieService.createListCharactersForMovie(movie));

        Assert.assertEquals(1, movie.getCast().size());
        Assert.assertEquals("Luke Skywalker", movie.getCast().get(0));
    }

    @Test
    public void testeCreateListCharactersBadRequestShouldLaunchException() {
        Movie movie = getMovieWithId1();

        try {
            People people = new People();
            people.setName("Luke Skywalker");

            mockRestTemplateExchange(people, HttpStatus.BAD_REQUEST);

            movie.setCast(movieService.createListCharactersForMovie(movie));
        } catch (ApiRequestException a) {
            Assert.assertEquals("Erro ao pesquisar o personagem da URL " + movie.getCharacters().get(0), a.getMessage());
        }
    }

    @Test
    public void getAllMovies() {
        Movie movie = getMovieWithId1();
        MovieWrapper movieWrapper = new MovieWrapper();
        movieWrapper.setMovies(Collections.singletonList(movie));

        mockRestTemplateExchange(movieWrapper, HttpStatus.OK);

        Mockito.doReturn(Collections.singletonList("http://swapi.dev/api/people/1")).when(movieService).createListCharactersForMovie(movie);

        List<Movie> movies = movieService.getAllMovies();

        Assert.assertEquals(1, movies.size());
        Assert.assertEquals(movie.getTitle(), movies.get(0).getTitle());
    }

    @Test
    public void getAllMoviesErrorRequestShouldLaunchException() {
        Movie movie = getMovieWithId1();
        MovieWrapper movieWrapper = new MovieWrapper();
        movieWrapper.setMovies(Collections.singletonList(movie));

        try {
            mockRestTemplateExchange(movieWrapper, HttpStatus.NOT_FOUND);
            List<Movie> movies = movieService.getAllMovies();
        } catch (ApiRequestException a) {
            Assert.assertEquals("Erro ao recuperar a lista de filmes", a.getMessage());
        }
    }

    private void mockRestTemplateExchange(Object object, HttpStatus httpStatus) {
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Object>>any())).thenReturn(new ResponseEntity<>(object, httpStatus));
    }

    private Movie getMovieWithId1() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("A New Hope");
        movie.setCharacters(Collections.singletonList("http://swapi.dev/api/people/1"));

        return movie;
    }
}

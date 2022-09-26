package com.digitalhouse.catalogservice.api.controller;

import java.util.List;

import com.digitalhouse.catalogservice.api.model.Catalogo;
import com.digitalhouse.catalogservice.api.service.CatalagoService;
import com.digitalhouse.catalogservice.api.service.SerieService;
import com.digitalhouse.catalogservice.domain.model.SerieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.digitalhouse.catalogservice.api.service.MovieService;
import com.digitalhouse.catalogservice.domain.model.MovieDTO;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    private final MovieService movieService;
    private final SerieService serieService;
    private final CatalagoService catalagoService;

    @Autowired
    public CatalogController(MovieService movieService,SerieService serieService,CatalagoService catalagoService) {
        this.movieService = movieService;
        this.serieService = serieService;
        this.catalagoService =catalagoService;
    }

    @GetMapping("/{genre}")
    public Catalogo getCatalog(@PathVariable String genre) {
        return catalagoService.findByGenre(genre);
    }

    @GetMapping("/movie/{genre}")
    public ResponseEntity<List<MovieDTO>> getGenre(@PathVariable String genre) {
        return movieService.findMovieByGenre(genre);
    }

    @GetMapping("/series/{genre}")
    public ResponseEntity<List<SerieDTO>> getSeries(@PathVariable String genre) {
        return serieService.findMovieByGenre(genre);
    }

    /*@PostMapping("/movie")
    public ResponseEntity<String> saveMovie(@RequestBody MovieDTO movieDTO) {
        movieService.saveMovie(movieDTO);
        return ResponseEntity.ok("la pelicula fue enviada a la cola");
    }

    @PostMapping("/series")
    public ResponseEntity<String> saveSerie(@RequestBody SerieDTO serieDTO) {
        serieService.saveSerie(serieDTO);
        return ResponseEntity.ok("la pelicula fue enviada a la cola");
    }*/
}

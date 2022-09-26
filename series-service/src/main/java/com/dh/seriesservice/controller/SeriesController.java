package com.dh.seriesservice.controller;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dh.seriesservice.model.Series;
import com.dh.seriesservice.service.SeriesService;

@RestController
@RequestMapping("/series")
public class SeriesController {

    private final SeriesService service;

    @Autowired
    public SeriesController(SeriesService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Series> seriesList = service.findAll();
        return seriesList.isEmpty()
            ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
            : ResponseEntity.ok(seriesList);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        Series series = service.findById(id);
        return Objects.isNull(series)
            ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
            : ResponseEntity.ok(series);
    }

    /*Obtener un listado de series por género. Endpoint: /series/{genre} [GET]*/
    @GetMapping("/{genre}")
    public ResponseEntity<?> findByGenre(@PathVariable String genre) {
        List<Series> seriesList = service.findByGenre(genre);
        return seriesList.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : ResponseEntity.ok(seriesList);
    }

    /*Agregar una nueva serie. Endpoint: /series [POST]*/
    @PostMapping
    public ResponseEntity<Series> saveSeries(@RequestBody Series series) {
        return ResponseEntity.ok().body(service.saveSeries(series));
    }
}

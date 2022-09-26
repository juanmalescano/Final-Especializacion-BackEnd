package com.digitalhouse.catalogservice.api.model;

import com.digitalhouse.catalogservice.domain.model.MovieDTO;
import com.digitalhouse.catalogservice.domain.model.SerieDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Catalogo {

    @Id
    private String genre;
    private List<MovieDTO> movies;
    private List<SerieDTO> serie;

    public Catalogo() {
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDTO> movies) {
        this.movies = movies;
    }

    public List<SerieDTO> getSerie() {
        return serie;
    }

    public void setSerie(List<SerieDTO> serie) {
        this.serie = serie;
    }
}

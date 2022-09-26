package com.digitalhouse.catalogservice.domain.model;


import java.io.Serializable;
import java.util.List;

public class SerieDTO implements Serializable {
    private String id;
    private String name;
    private String genre;
    private List<SeasonsDTO> seasons;

    public SerieDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<SeasonsDTO> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<SeasonsDTO> seasons) {
        this.seasons = seasons;
    }

    @Override
    public String toString() {
        return "SerieDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}

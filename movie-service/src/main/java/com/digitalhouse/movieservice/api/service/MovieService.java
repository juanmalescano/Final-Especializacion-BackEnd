package com.digitalhouse.movieservice.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.digitalhouse.movieservice.domain.models.Movie;
import com.digitalhouse.movieservice.domain.repositories.MovieRepository;
import com.digitalhouse.movieservice.util.RedisUtils;

@Service
public class MovieService {

    private static final Logger LOG = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository repository;
    private final RedisUtils redisUtils;
    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.general.name}")
    private String movieQueue;

    @Autowired
    public MovieService(MovieRepository movieRepository, RedisUtils redisUtils,RabbitTemplate rabbitTemplate) {
        this.repository = movieRepository;
        this.redisUtils = redisUtils;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Movie> findByGenre(String genre) {
        LOG.info("se van a buscar las peliculas por género");
        return repository.findByGenre(genre);
    }

    public List<Movie> findByGenre(String genre, Boolean throwError) {
        LOG.info("se van a buscar las peliculas por género");
        if (throwError) {
            LOG.error("Hubo un error al buscar las películas");
            throw new RuntimeException();
        }
        return repository.findByGenre(genre);
    }

    public Movie saveMovie(Movie movie) {
        Movie movieInsert = repository.save(movie);
        rabbitTemplate.convertAndSend(movieQueue,movieInsert);
        return movieInsert;
    }
}

package com.digitalhouse.catalogservice.api.service;

import java.util.ArrayList;
import java.util.List;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.digitalhouse.catalogservice.api.client.MovieClient;
import com.digitalhouse.catalogservice.domain.model.MovieDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

@Service
public class MovieService {

    @Value("${queue.general.name}")
    private String generalQueue;

    private final Logger LOG = LoggerFactory.getLogger(MovieService.class);

    private final MovieClient movieClient;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MovieService(MovieClient movieClient, RabbitTemplate rabbitTemplate) {
        this.movieClient = movieClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public ResponseEntity<List<MovieDTO>> findMovieByGenre(String genre) {
        LOG.info("Se busca el listado de series del genero " + genre + " al servicio movie-service...");
        return movieClient.getMovieByGenre(genre);
    }
}

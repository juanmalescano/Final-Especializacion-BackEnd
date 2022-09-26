package com.digitalhouse.catalogservice.api.service;

import com.digitalhouse.catalogservice.api.client.SerieClient;
import com.digitalhouse.catalogservice.domain.model.MovieDTO;
import com.digitalhouse.catalogservice.domain.model.SerieDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SerieService {
    @Value("${queue.general.name}")
    private String generalQueue;

    private final Logger LOG = LoggerFactory.getLogger(MovieService.class);

    private final SerieClient serieClient;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SerieService(SerieClient serieClient, RabbitTemplate rabbitTemplate) {
        this.serieClient = serieClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public ResponseEntity<List<SerieDTO>> findMovieByGenre(String genre) {
        LOG.info("Se busca el listado de series del genero " + genre + " al servicio series-service...");
        return serieClient.getSerieByGenre(genre);
    }
}

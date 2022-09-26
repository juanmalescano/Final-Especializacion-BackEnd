package com.digitalhouse.catalogservice.api.client;

import com.digitalhouse.catalogservice.domain.model.SerieDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "series-service")
public interface SerieClient {
    @GetMapping("/series/{genre}")
    ResponseEntity<List<SerieDTO>> getSerieByGenre(@PathVariable(value = "genre") String genre);
}

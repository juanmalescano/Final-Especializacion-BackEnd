package com.dh.seriesservice.repository;

import com.dh.seriesservice.model.Series;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends MongoRepository<Series, String> {
    List<Series> findByGenre(String series);
}

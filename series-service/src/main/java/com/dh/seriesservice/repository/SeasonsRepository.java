package com.dh.seriesservice.repository;

import com.dh.seriesservice.model.Seasons;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeasonsRepository extends MongoRepository<Seasons, String> {
}

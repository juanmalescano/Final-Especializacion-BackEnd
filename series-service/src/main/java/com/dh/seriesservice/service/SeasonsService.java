package com.dh.seriesservice.service;

import com.dh.seriesservice.model.Seasons;
import com.dh.seriesservice.repository.SeasonsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeasonsService {

    private static final Logger LOG = LoggerFactory.getLogger(SeriesService.class);
    private final SeasonsRepository seasonsRepository;

    @Autowired
    public SeasonsService(SeasonsRepository seasonsRepository) {
        this.seasonsRepository = seasonsRepository;
    }

    public Seasons saveSeason(Seasons seasons){
        return seasonsRepository.save(seasons);
    }
}

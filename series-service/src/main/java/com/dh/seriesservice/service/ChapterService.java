package com.dh.seriesservice.service;

import com.dh.seriesservice.model.Chapter;
import com.dh.seriesservice.repository.ChapterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterService {

    private static final Logger LOG = LoggerFactory.getLogger(SeriesService.class);
    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    public Chapter saveChapter(Chapter chapter){
        return chapterRepository.save(chapter);
    }
}

package com.dh.seriesservice.service;

import java.util.ArrayList;
import java.util.List;

import com.dh.seriesservice.model.Chapter;
import com.dh.seriesservice.model.Seasons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.dh.seriesservice.model.Series;

import com.dh.seriesservice.repository.SeriesRepository;

@Service
public class SeriesService {

    @Value("${queue.general.name}")
    private String generalQueue;

    private static final Logger LOG = LoggerFactory.getLogger(SeriesService.class);
    private final RabbitTemplate rabbitTemplate;
    private final SeriesRepository seriesRepository;
    private final SeasonsService seasonsService;
    private final ChapterService chapterService;

    @Autowired
    public SeriesService(SeriesRepository seriesRepository,RabbitTemplate rabbitTemplate,SeasonsService seasonsService,ChapterService chapterService) {
        this.seriesRepository = seriesRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.seasonsService = seasonsService;
        this.chapterService = chapterService;
    }

    public Series findById(String id) {
        return seriesRepository.findById(id)
            .orElse(null);
    }

    public List<Series> findAll() {
        return seriesRepository.findAll();
    }

    /********************************************************************************************
     Guardar Serie
     ********************************************************************************************/
    public Series saveSeries(Series series) {
        List<Seasons> list_seasons = series.getSeasons();
        List<Seasons> list_seasons_insertados = new ArrayList<>();

        //recorro la lista de seasons
        for (Seasons season : list_seasons) {

            //Obtengo la lista de Chapters
            List<Chapter> list_chapter_request = season.getChapters();
            List<Chapter> list_chapter_insertados = new ArrayList<>();

            //Recorro Chapters y los inserto para que obtengan un ID
            for (Chapter chapter: list_chapter_request) {
                list_chapter_insertados.add(chapterService.saveChapter(chapter));
            }

            //Con la lista de Chapters insertados, se los seteo a la season de la iteraccion actual y inserto la season para que obtenga un ID
            season.setChapters(list_chapter_insertados);
            list_seasons_insertados.add(seasonsService.saveSeason(season));
        }

        //Con la lista seasons insertadas, se las seteo a la serie para luego ser persistida en la db
        series.setSeasons(list_seasons_insertados);
        Series serie =   seriesRepository.save(series);

        LOG.info("Se guarda serie en mongodb del microservicio Serie" + series.getName());

        //Mando serie por mensaje por rabbitmq para que la detecte Catalogo y la persista
        rabbitTemplate.convertAndSend(generalQueue,serie);
        return serie;
    }

    /******************************************************************************************
     Obtener un listado de series por género.
     ******************************************************************************************/
    public List<Series> findByGenre(String genre){
        LOG.info("se van a buscar las series por género");
        return seriesRepository.findByGenre(genre);
    }
}

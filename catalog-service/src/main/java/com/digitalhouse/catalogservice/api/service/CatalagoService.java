package com.digitalhouse.catalogservice.api.service;

import com.digitalhouse.catalogservice.api.model.Catalogo;
import com.digitalhouse.catalogservice.api.repository.CatalogoRepository;
import com.digitalhouse.catalogservice.domain.model.MovieDTO;
import com.digitalhouse.catalogservice.domain.model.SerieDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalagoService {

    private static final Logger LOG = LoggerFactory.getLogger(CatalagoService.class);
    private final RabbitTemplate rabbitTemplate;
    private final CatalogoRepository catalogoRepository;
    private final MovieService movieService;
    private final SerieService serieService;


    @Autowired
    public CatalagoService(RabbitTemplate rabbitTemplate,CatalogoRepository catalogoRepository,MovieService movieService,SerieService serieService){
        this.rabbitTemplate = rabbitTemplate;
        this.catalogoRepository = catalogoRepository;
        this.movieService = movieService;
        this.serieService = serieService;
    }

    /**************************************************************************************************
     Aplico CircuitBreaker a este metodo indicandole a que metodo tiene que ir cuando pase las n excepciones.
     Me parece un metodo clitico ya que internamente se hace peticiones a dos microservicios que podrian estar
     eventualmente caidos.
     **************************************************************************************************/
    @CircuitBreaker(name = "catalogs", fallbackMethod = "catalogsFallbackMethod")
    @Retry(name = "catalogs")
    public Catalogo findByGenre(String genre){
        //Busco las peliculas del genero ingresado por Feign
       ResponseEntity<List<MovieDTO>> listMoviesByGenre = movieService.findMovieByGenre(genre);

        //Busco series del genero ingresado por Feign
        List<SerieDTO> listSeriesByGenre = serieService.findMovieByGenre(genre).getBody();

        //Busco si existe un catalogo
        Catalogo catalogo = catalogoRepository.findByGenre(genre);

        //Si no existe catalogo lo creo y seteo genero
        if(catalogo == null){
            catalogo = new Catalogo();
            catalogo.setGenre(genre);
        }

        //Actualizo las peliculas y series del catalogo que tenia con las que traje por Feign
        catalogo.setMovies(listMoviesByGenre.getBody());
        catalogo.setSerie(listSeriesByGenre);

        //Guardo en mongodb y retorno
        return catalogoRepository.save(catalogo);
    }

    /** FallbackMethod de circuitbreaker, si hay excepciones en el metodo findByGenre retorno un Catalogo vacio*/
    private Catalogo catalogsFallbackMethod(CallNotPermittedException exception){
        return  new Catalogo();
    }

    /********************************************************************************************
     Agregar RabbitMQ y escuchar los mensajes que envían movie-service y serie-service.
     En caso de recibir un mensaje de algún servicio, actualizar el listado correspondiente, ya
     sea las películas o las series
     *********************************************************************************************/
    //Metodo que escucha los mensajes de la cola GENERAL
    @RabbitListener(queues = "${queue.general.name}")
    public void saveMovieAndSeries(Object obj) {
       try {
           ObjectMapper objectMapper = new ObjectMapper();
           Jackson2JsonMessageConverter a = new Jackson2JsonMessageConverter();
           Catalogo cat_db = null;

           //Verifico de que microservicio recibio el mensaje para saber a que tipo de clase mapear
           if (((Message) obj).getMessageProperties().getHeaders().get("__TypeId__").toString().equals("com.digitalhouse.movieservice.domain.models.Movie")) {

               //Mapeo mensaje recibido a la clase MovieDTO para persistirlo en la db de Catalogo
               MovieDTO movie = objectMapper.convertValue(a.fromMessage((Message) obj), MovieDTO.class);
               LOG.info("Se recibio una pelicula a través de rabbit desde el microservicio de movie-service " + movie.toString());

               List<MovieDTO> list=null;
               cat_db = catalogoRepository.findByGenre(movie.getGenre());

               if (cat_db != null) {
                   list = cat_db.getMovies();
               } else {
                   cat_db = new Catalogo();
                   cat_db.setGenre(movie.getGenre());
               }

               if(list==null){
                   list = new ArrayList<>();
               }

               list.add(movie);
               cat_db.setMovies(list);
           } else {
               //Mapeo mensaje recibido a la clase SerieDTO para persistirlo en la db de Catalogo
               SerieDTO serie = objectMapper.convertValue(a.fromMessage((Message) obj), SerieDTO.class);
               LOG.info("Se recibio una serie a través de rabbit desde el microservicio de series " + serie.toString());

               List<SerieDTO> list = null;
               cat_db = catalogoRepository.findByGenre(serie.getGenre());

               if (cat_db != null) {
                   list = cat_db.getSerie();
               } else {
                   cat_db = new Catalogo();
                   cat_db.setGenre(serie.getGenre());
               }

               if(list==null){
                   list = new ArrayList<>();
               }

               list.add(serie);
               cat_db.setSerie(list);
           }

           catalogoRepository.save(cat_db);
       }catch (Exception ex){
           LOG.info("Error: " + ex.getMessage());
       }
   }
}

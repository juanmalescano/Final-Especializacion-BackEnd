package com.digitalhouse.catalogservice.api.repository;

import com.digitalhouse.catalogservice.api.model.Catalogo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoRepository extends MongoRepository<Catalogo, String> {
    Catalogo findByGenre(String genre);
}

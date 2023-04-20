package com.company.inventory.dao;

import com.company.inventory.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

//esta interface tiene para poder hacer crear, leer, updatear y borrar sobre los objetos definidos (Product) y el Long es sobre el seriazable
public interface IProductDao extends CrudRepository<Product, Long> {

    //nos falta este método así que los creamos, el resto lo estamos rescatando de CrudRepository
    //es buscar alguna parte que se parezca
    @Query("SELECT p from Product p WHERE name LIKE %?1%")
    List<Product> findByName(String name);

    //mismo que el de arriba pero son anotaciones JPA
    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //ignorando mayúsculas y minúculas
    List<Product> findByNameContaining(String Name);

}

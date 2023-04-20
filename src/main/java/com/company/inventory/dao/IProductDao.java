package com.company.inventory.dao;

import com.company.inventory.model.Product;
import org.springframework.data.repository.CrudRepository;

//esta interface tiene para poder hacer crear, leer, updatear y borrar sobre los objetos definidos (Product) y el Long es sobre el seriazable
public interface IProductDao extends CrudRepository<Product, Long> {

}

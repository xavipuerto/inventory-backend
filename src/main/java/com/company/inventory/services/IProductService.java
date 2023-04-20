package com.company.inventory.services;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import org.springframework.http.ResponseEntity;

public interface IProductService {
    public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId);
    public ResponseEntity<ProductResponseRest> searchById(Long id);
}

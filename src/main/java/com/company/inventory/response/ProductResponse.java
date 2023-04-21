package com.company.inventory.response;

import com.company.inventory.model.Product;
import lombok.Data;

import java.util.List;

//para responder una lista de productos
@Data
public class ProductResponse {

    //devolvemos una lista de productos
    List<Product> products;
}

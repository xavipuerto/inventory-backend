package com.company.inventory.response;
//2
import com.company.inventory.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {

    //responde una lista de categorias
    private List<Category> category;

}

package com.company.inventory.services;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    //vamos a inyectar el categorydao mediante el categorydao (en el category lo hemos hecho de otra forma -> el autowired)
    private ICategoryDao categoryDao;

    private IProductDao productDao;

    //generamos el constructor
    public ProductServiceImpl(ICategoryDao categoryDao, IProductDao productDao) {
        super();
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @Override
    //@Transactional (readOnly = true)
    public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {

        //declaramos en primer luegar el objeto de respuesta
        //objeto de respuesta del servicio
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try{
            //recibimos la categoria id que está relacionado el producto
            //tengo que buscarla dentro de buscar por id
            //inyectamos dependencias del icategorydao
            Optional<Category> category = categoryDao.findById(categoryId);

            if( category.isPresent()) {
                product.setCategory(category.get());
            } else {
                response.setMetadata("Respuesta KO", "-1", "Categoria no encontrada asociada al producto ");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

            //guardamos el producto
            Product productSaved = productDao.save(product);

            if(productSaved != null){
                list.add(productSaved);
                response.getProduct().setProducts(list);
                response.setMetadata("Respuesta OK", "00", "Producto guardado");
            }else{
                response.setMetadata("Respuesta KO", "00", "Producto no guardado");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            //colocamos mas info con la siguiente sentencia
            e.getStackTrace();

            response.setMetadata("Respuesta KO", "-1", "Error al guardado producto");
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional (readOnly = true)
    public ResponseEntity<ProductResponseRest> searchById(Long id) {

        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try{
            //search producto by id
            Optional<Product> product = productDao.findById(id);

            if (product.isPresent()){
                //recuperamos la imagen en base64
                //se rescatan en tipo byte
                byte[] imagenDescompressed = Util.decompressZLib(product.get().getPicture());
                product.get().setPicture(imagenDescompressed);
                list.add(product.get());
                response.getProduct().setProducts(list);
                response.setMetadata("Respuesta OK", "00", "Producto encontrado");
            }else{
                response.setMetadata("Respuesta KO", "-1", "Producto no encontrado por Id");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

        }catch (Exception e){
            e.getStackTrace();
            response.setMetadata("Respuesta KO", "-1", "Producto no guardado. Error");
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
}

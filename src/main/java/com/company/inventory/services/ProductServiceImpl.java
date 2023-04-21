package com.company.inventory.services;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.response.ResponseRest;
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

    //@Transactional es hacer el commit en bbdd o rollback
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

    @Override
    @Transactional (readOnly = true)
    public ResponseEntity<ProductResponseRest> searchByName(String name) {

        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();
        List<Product> listAux = new ArrayList<>();

        try {
            //puedo usar cualquier que hemos definido en IProductService.java --> IProductDao.java
            //listaux = productDao.findByName(name);
            listAux = productDao.findByNameContaining(name);

            if (listAux.size() > 0){
            //if(!listAux.isEmpty()){

                //voy a recorrer la listaux para descomprimir cada una de las imagenes
                //lo de (p) -> { .... es una función Lambda
                listAux.stream().forEach( (p) -> {
                    //aqui descomprimimos
                    byte[] imageDescompressed = Util.decompressZLib(p.getPicture());
                    p.setPicture(imageDescompressed);
                    //list es la lista que voy a mandar hacia fuera
                    list.add(p);
                } );

                //en el response mandamos la lista de producto(s)
                response.getProduct().setProducts(list);
                response.setMetadata("Respuesta OK", "00", "Productos encontrados");

            }else{
                response.setMetadata("respuesta KO", "00", "Productos no encontrados");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

        }catch (Exception e){
            e.getStackTrace();
            response.setMetadata("Respuesta KO", "-1", "Error al buscar producto por nombre");
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductResponseRest> deleteById(Long id) {
        ProductResponseRest response = new ProductResponseRest();
        try{
            productDao.deleteById(id);
            response.setMetadata("Respuesta OK", "00", "Producto Borrado");
        }catch (Exception e){
            e.getStackTrace();
            response.setMetadata("Resultado KO", "-1", "Error al borrar el producto");
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> search() {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();
        List<Product> listAux = new ArrayList<>();

        try{

            //obtenemos la busqueda en una lista de todos los productos
            listAux = (List<Product>) productDao.findAll();

            if (!listAux.isEmpty()){

                listAux.stream().forEach((p) -> {
                    byte[] imageDescompressed = Util.decompressZLib(p.getPicture());
                    p.setPicture(imageDescompressed);

                    list.add(p);
                });

                response.getProduct().setProducts(list);

                response.setMetadata("Respuesta OK", "00", "Productos Encontrados");
            }else{
                response.setMetadata("Respuesta KO", "-1", "Productos no Encontrados");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }



        }catch (Exception e){
            e.getStackTrace();
            response.setMetadata("Respuesta KO", "-1", "Error en la búsqueda de productos");
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }

    //@Transactional es qué hacer, si commit o rollback
    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long id) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try{

            //busco la nueva categoria donde quiero que se actuace el producto
            Optional<Category> category = categoryDao.findById(categoryId);

            if (category.isPresent()){
                product.setCategory(category.get());
            }else{
                response.setMetadata("Respuesta KO", "-1", "Categoria no encontrada para acualizar");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

            //ahora actualizamos la categoria que vamos a actualizar
            Optional<Product> productSearch = productDao.findById(id);

            if (productSearch.isPresent()){
                //se actualizara el producto
                productSearch.get().setAccount(product.getAccount());
                productSearch.get().setName(product.getName());
                productSearch.get().setPrice(product.getPrice());
                productSearch.get().setPicture(product.getPicture());
                productSearch.get().setCategory(product.getCategory());

                //con esto se guarda el producto en bbdd
                Product productToUpdate = productDao.save(productSearch.get());

                if (productToUpdate != null){
                    list.add(productToUpdate);
                    response.getProduct().setProducts(list);
                    response.setMetadata("Respuesta OK", "00", "Producto guardado");
                }else{
                    response.setMetadata("Respuesta KO", "-1", "Producto no actualizado");
                    return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
                }


            }else{
                response.setMetadata("Respuesta KO", "-1", "Producto no guardado");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

        }catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("Respuesta KO", "-1", "Error en la actualizacion de productos");
        }

        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
}

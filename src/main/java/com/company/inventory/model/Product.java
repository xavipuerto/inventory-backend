package com.company.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

//entity es para el mapeo con la base de datos
//table es la referencia a la tabla
//implement para serializable
//data es para evitarme hacer los setter y getter, los autogenera lombok
@Data
@Entity
@Table(name = "product")
public class Product implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7461389651533509262L;

    //id de la tabla se genera automaticamente con las dos siguientes nomenclaturas
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    //account es cantidad de productos
    private int account;

    //hacemos referencia a ese objeto en bbdd con la tabla de categoria para poder vincularla con producto
    //1 producto va a pertenecer a una categoria. 1 categoria puede tener varios productos
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JsonIgnoreProperties es para ignorar que propiedades quiero que salgan
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
    private Category category;

    //vamos a almacenar las fotos en base64
    //@Column( name = "picture", columnDefinition = "longblob")
    @Lob
    @Basic (fetch = FetchType.LAZY)
    @Column( name = "picture" , columnDefinition = "longblob")
    private byte[] picture;
}

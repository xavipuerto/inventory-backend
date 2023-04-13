package com.company.inventory.model;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data  //esto es para los seter y los getter mediante lombok
@Entity
@Table(name="category")
public class Category implements Serializable {

    private static final long serialVersionUID = -4310027227752446841L;

    @Id //para indicar que va a ser nuestro identificador
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id de nuestra clase
    private String name; //nombre de nuestra clase
    private String description; //description



}

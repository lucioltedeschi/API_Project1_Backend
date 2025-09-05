package com.uade.tpo.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "productos")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private float precio;

    @Column(nullable = false)
    private int stock;

    @Column
    private float descuento; // porcentaje, por ejemplo: 10.0 = 10%

    @Column
    private String categoria;
}
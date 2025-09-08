package com.uade.tpo.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "order_details")
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product producto;

    @Column
    private int cantidad;

    @Column
    private float precioUnitario;

    @Column
    private float descuentoProducto; // porcentaje o monto aplicado

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


    public DetalleOrden() {

    }
}

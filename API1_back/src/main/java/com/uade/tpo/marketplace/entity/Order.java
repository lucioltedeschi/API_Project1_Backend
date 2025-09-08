package com.uade.tpo.marketplace.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fecha;

    @Column
    private float precioTotal;

    @Column
    private float descuentoCupon; // porcentaje de descuento global

    @Column
    private String codigoCupon; // opcional: para identificar el cupón


    // Relación con usuario (N:1)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Nombre de usuario visible
    @Column
    private String username;

    // Relación con detalle de orden (1:N)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalleOrden = new ArrayList<>();

    // ---------------- MÉTODOS ÚTILES ---------------- //

    // Agregar un detalle a la orden
    public void addDetalle(DetalleOrden detalle) {
        detalle.setOrder(detalle.getOrder()); // vincula el detalle a la orden
        this.detalleOrden.add(detalle);
        recalcularTotal();
    }

    // Eliminar un detalle de la orden
    public void removeDetalle(DetalleOrden detalle) {
        this.detalleOrden.remove(detalle);
        detalle.setOrder(null);
        recalcularTotal();
    }

    // Recalcular el total de la orden
    public void recalcularTotal() {
        float total = 0f;

        for (DetalleOrden detalle : detalleOrden) {
            float precioBruto = detalle.getPrecioUnitario() * detalle.getCantidad();
            float descuento = (detalle.getDescuentoProducto() / 100f) * precioBruto;
            total += precioBruto - descuento;
        }

        // aplicar cupón global si existe
        if (descuentoCupon > 0) {
            total -= (descuentoCupon / 100f) * total;
        }

        this.precioTotal = total;
    }
}

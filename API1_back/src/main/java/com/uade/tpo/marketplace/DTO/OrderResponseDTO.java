package com.uade.tpo.marketplace.DTO;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long id;
    private String fecha;
    private Float precioTotal;
    private Float descuentoCupon;
    private String codigoCupon;
    private String userEmail;
}

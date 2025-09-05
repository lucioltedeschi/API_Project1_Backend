package com.uade.tpo.demo.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private String fecha;
    private String codigoCupon;
    private List<DetalleOrdenDTO> detalleOrden;
}
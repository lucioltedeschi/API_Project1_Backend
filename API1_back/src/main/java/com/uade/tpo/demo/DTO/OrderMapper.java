package com.uade.tpo.demo.DTO;

import com.uade.tpo.demo.entity.Order;

public class OrderMapper {
    public static OrderResponseDTO toDto(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setFecha(order.getFecha());
        dto.setPrecioTotal(order.getPrecioTotal());
        dto.setDescuentoCupon(order.getDescuentoCupon());
        dto.setCodigoCupon(order.getCodigoCupon());
        dto.setUserEmail(order.getUser().getEmail());

        return dto;
    }
}
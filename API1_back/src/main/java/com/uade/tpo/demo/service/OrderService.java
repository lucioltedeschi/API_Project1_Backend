package com.uade.tpo.demo.service;

import com.uade.tpo.demo.DTO.OrderRequestDTO;
import com.uade.tpo.demo.entity.*;
import com.uade.tpo.demo.repository.OrderRepository;
import com.uade.tpo.demo.repository.UserRepository;
import com.uade.tpo.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    @Transactional
    public Order crearOrden(Order order) {
        User user = getCurrentUser(); // sacamos usuario desde JWT
        order.setUser(user);

        order.recalcularTotal();
        order.getDetalleOrden().forEach(det -> det.setOrder(order)); // corregido: se pasa el objeto, no el ID como String

        return orderRepository.save(order);
    }

    public Order crearOrdenDesdeDTO(OrderRequestDTO dto) {
        User user = getCurrentUser();

        Order order = new Order();
        order.setFecha(dto.getFecha());
        order.setCodigoCupon(dto.getCodigoCupon());
        order.setUser(user);

        List<DetalleOrden> detalles = dto.getDetalleOrden().stream()
                .map(det -> {
                    Product producto = productRepository.findById(det.getProductoId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                    DetalleOrden detalle = new DetalleOrden();
                    detalle.setProducto(producto);
                    detalle.setCantidad(det.getCantidad());
                    detalle.setPrecioUnitario(producto.getPrecio()); // o como lo tengas
                    detalle.setDescuentoProducto(producto.getDescuento()); // si aplica
                    detalle.setOrder(order);
                    return detalle;
                })
                .collect(Collectors.toList());

        order.setDetalleOrden(detalles);
        order.recalcularTotal();

        return orderRepository.save(order);
    }







    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    public List<Order> getOrdersByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return orderRepository.findByUser(user);
    }

    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth instanceof UsernamePasswordAuthenticationToken)) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        String email = auth.getName(); // viene del token
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}

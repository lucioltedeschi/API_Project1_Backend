package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.DTO.OrderRequestDTO;
import com.uade.tpo.marketplace.entity.*;
import com.uade.tpo.marketplace.repository.OrderRepository;
import com.uade.tpo.marketplace.repository.UserRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Order crearOrden(Order order) {
        User user = getCurrentUser();
        order.setUser(user);

        order.recalcularTotal();
        order.getDetalleOrden().forEach(det -> det.setOrder(order));

        return orderRepository.save(order);
    }

    @Override
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
                    detalle.setPrecioUnitario(producto.getPrice());
                    detalle.setDescuentoProducto(producto.getDiscount());
                    detalle.setOrder(order);
                    return detalle;
                })
                .collect(Collectors.toList());

        order.setDetalleOrden(detalles);
        order.recalcularTotal();

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    @Override
    public List<Order> getOrdersByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return orderRepository.findByUser(user);
    }

    @Override
    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth instanceof UsernamePasswordAuthenticationToken)) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
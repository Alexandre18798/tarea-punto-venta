package com.punto.venta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.punto.venta.entity.Cliente;
import com.punto.venta.entity.Pedido;

public interface PedidoRepository
        extends JpaRepository<Pedido, Integer> {

    boolean existsByIdClienteAndEstadoPedidoFalse(
            Cliente cliente
    );

    List<Pedido> findByEstadoTrue();

    List<Pedido> findByEstadoTrueAndIdClienteNombreContainingIgnoreCase(
            String nombre
    );

    List<Pedido> findTop2ByEstadoTrueAndIdClienteNombreContainingIgnoreCase(
            String nombre
    );
}
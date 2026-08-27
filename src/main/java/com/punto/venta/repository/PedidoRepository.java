package com.punto.venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.punto.venta.entity.Cliente;
import com.punto.venta.entity.Pedido;

public interface PedidoRepository
        extends JpaRepository<Pedido, Integer> {

    boolean existsByIdClienteAndEstadoPedidoFalse(
            Cliente cliente
    );
}
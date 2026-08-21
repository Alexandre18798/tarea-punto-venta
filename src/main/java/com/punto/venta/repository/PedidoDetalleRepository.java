package com.punto.venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.punto.venta.entity.PedidoDetalle;

public interface PedidoDetalleRepository
        extends JpaRepository<PedidoDetalle, Integer> {
    
}

package com.punto.venta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.punto.venta.entity.Pedido;
import com.punto.venta.entity.PedidoDetalle;
import com.punto.venta.entity.Producto;

public interface PedidoDetalleRepository
        extends JpaRepository<PedidoDetalle, Integer> {

    boolean existsByIdPedidoAndIdProducto(
            Pedido pedido,
            Producto producto
    );

    List<PedidoDetalle> findByEstadoTrue();

    List<PedidoDetalle> findByEstadoTrueAndIdProductoNombreContainingIgnoreCase(
            String nombre
    );

    List<PedidoDetalle> findTop2ByEstadoTrueAndIdProductoNombreContainingIgnoreCase(
            String nombre
    );
}
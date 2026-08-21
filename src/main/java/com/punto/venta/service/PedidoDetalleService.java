package com.punto.venta.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.punto.venta.dto.PedidoDetalleDTO;
import com.punto.venta.entity.Pedido;
import com.punto.venta.entity.PedidoDetalle;
import com.punto.venta.entity.Producto;
import com.punto.venta.repository.PedidoDetalleRepository;
import com.punto.venta.repository.PedidoRepository;
import com.punto.venta.repository.ProductoRepository;

@Service
public class PedidoDetalleService {
    
    private final PedidoDetalleRepository pedidoDetalleRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoDetalleService(
       PedidoDetalleRepository pedidoDetalleRepository,
       PedidoRepository pedidoRepository,
       ProductoRepository productoRepository) {

        this.pedidoDetalleRepository = pedidoDetalleRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
       }

public List<PedidoDetalleDTO> listarDetalles() {
        return pedidoDetalleRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PedidoDetalleDTO crear(PedidoDetalleDTO dto) {

        Pedido pedido = pedidoRepository
                .findById(dto.getIdPedido())
                .orElseThrow(() ->
                        new RuntimeException("El pedido no existe"));

        Producto producto = productoRepository
                .findById(dto.getIdProducto())
                .orElseThrow(() ->
                        new RuntimeException("El producto no existe"));

        PedidoDetalle detalle = new PedidoDetalle();

        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());

        BigDecimal subtotal = dto.getSubtotal();

        if (subtotal == null) {
            subtotal = dto.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(dto.getCantidad()));
        }

        detalle.setSubtotal(subtotal);
        detalle.setIdPedido(pedido);
        detalle.setIdProducto(producto);

        PedidoDetalle guardado =
                pedidoDetalleRepository.save(detalle);

        return convertirADTO(guardado);
    }

    private PedidoDetalleDTO convertirADTO(
            PedidoDetalle detalle) {

        PedidoDetalleDTO dto = new PedidoDetalleDTO();

        dto.setIdPedidoDetalle(
                detalle.getIdPedidoDetalle());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());

        if (detalle.getIdPedido() != null) {
            dto.setIdPedido(
                    detalle.getIdPedido().getIdPedido());
        }

        if (detalle.getIdProducto() != null) {
            dto.setIdProducto(
                    detalle.getIdProducto().getIdProducto());
        }

        return dto;
    }
}
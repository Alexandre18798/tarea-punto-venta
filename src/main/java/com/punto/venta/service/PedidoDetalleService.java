package com.punto.venta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.punto.venta.dto.PedidoDetalleDTO;
import com.punto.venta.entity.Pedido;
import com.punto.venta.entity.PedidoDetalle;
import com.punto.venta.entity.Producto;
import com.punto.venta.repository.PedidoDetalleRepository;

@Service
public class PedidoDetalleService {

    private final PedidoDetalleRepository pedidoDetalleRepository;

    public PedidoDetalleService(
            PedidoDetalleRepository pedidoDetalleRepository) {

        this.pedidoDetalleRepository =
                pedidoDetalleRepository;
    }

    public List<PedidoDetalleDTO> listarTodos() {

        return pedidoDetalleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDetalleDTO> mostrarActivos() {

        return pedidoDetalleRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDetalleDTO> mostrarActivosFiltro(
            String nombre) {

        return pedidoDetalleRepository
                .findByEstadoTrueAndIdProductoNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDetalleDTO> mostrarActivosFiltroTop(
            String nombre) {

        return pedidoDetalleRepository
                .findTop2ByEstadoTrueAndIdProductoNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PedidoDetalleDTO crear(
            PedidoDetalleDTO dto) {

        Pedido pedido = new Pedido();
        pedido.setIdPedido(dto.getIdPedido());

        Producto producto = new Producto();
        producto.setIdProducto(
                dto.getIdProducto()
        );

        boolean duplicado =
                pedidoDetalleRepository
                        .existsByIdPedidoAndIdProducto(
                                pedido,
                                producto
                        );

        if (duplicado) {
            throw new RuntimeException(
                    "El detalle de pedido ya existe"
            );
        }

        return convertToDTO(
                pedidoDetalleRepository.save(
                        convertToEntity(dto)
                )
        );
    }

    public PedidoDetalleDTO actualizar(
            Integer idPedidoDetalle,
            PedidoDetalleDTO dto) {

        PedidoDetalle detalleExistente =
                pedidoDetalleRepository
                        .findById(idPedidoDetalle)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Detalle de pedido no encontrado"
                                )
                        );

        if (dto.getIdPedido() != null) {

            Pedido pedido = new Pedido();
            pedido.setIdPedido(
                    dto.getIdPedido()
            );

            detalleExistente.setIdPedido(pedido);
        }

        if (dto.getIdProducto() != null) {

            Producto producto = new Producto();
            producto.setIdProducto(
                    dto.getIdProducto()
            );

            detalleExistente.setIdProducto(
                    producto
            );
        }

        if (dto.getCantidad() != null) {
            detalleExistente.setCantidad(
                    dto.getCantidad()
            );
        }

        if (dto.getPrecioUnitario() != null) {
            detalleExistente.setPrecioUnitario(
                    dto.getPrecioUnitario()
            );
        }

        if (dto.getSubtotal() != null) {
            detalleExistente.setSubtotal(
                    dto.getSubtotal()
            );
        }

        if (dto.getEstado() != null) {
            detalleExistente.setEstado(
                    dto.getEstado()
            );
        }

        return convertToDTO(
                pedidoDetalleRepository.save(
                        detalleExistente
                )
        );
    }

    public PedidoDetalleDTO anular(
            Integer idPedidoDetalle) {

        PedidoDetalle detalleExistente =
                pedidoDetalleRepository
                        .findById(idPedidoDetalle)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Detalle de pedido no encontrado"
                                )
                        );

        detalleExistente.setEstado(false);

        return convertToDTO(
                pedidoDetalleRepository.save(
                        detalleExistente
                )
        );
    }

    public void eliminar(Integer idPedidoDetalle) {

        if (!pedidoDetalleRepository
                .existsById(idPedidoDetalle)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Detalle de pedido no encontrado"
            );
        }

        pedidoDetalleRepository.deleteById(
                idPedidoDetalle
        );
    }

    private PedidoDetalleDTO convertToDTO(
            PedidoDetalle detalle) {

        PedidoDetalleDTO dto =
                new PedidoDetalleDTO();

        dto.setIdPedidoDetalle(
                detalle.getIdPedidoDetalle()
        );

        dto.setIdPedido(
                detalle.getIdPedido()
                        .getIdPedido()
        );

        dto.setIdProducto(
                detalle.getIdProducto()
                        .getIdProducto()
        );

        dto.setCantidad(
                detalle.getCantidad()
        );

        dto.setPrecioUnitario(
                detalle.getPrecioUnitario()
        );

        dto.setSubtotal(
                detalle.getSubtotal()
        );

        dto.setEstado(
                detalle.getEstado()
        );

        return dto;
    }

    private PedidoDetalle convertToEntity(
            PedidoDetalleDTO dto) {

        PedidoDetalle detalle =
                new PedidoDetalle();

        detalle.setIdPedidoDetalle(
                dto.getIdPedidoDetalle()
        );

        Pedido pedido = new Pedido();
        pedido.setIdPedido(
                dto.getIdPedido()
        );

        detalle.setIdPedido(pedido);

        Producto producto = new Producto();
        producto.setIdProducto(
                dto.getIdProducto()
        );

        detalle.setIdProducto(producto);

        detalle.setCantidad(
                dto.getCantidad()
        );

        detalle.setPrecioUnitario(
                dto.getPrecioUnitario()
        );

        detalle.setSubtotal(
                dto.getSubtotal()
        );

        detalle.setEstado(
                dto.getEstado() != null
                        ? dto.getEstado()
                        : true
        );

        return detalle;
    }
}
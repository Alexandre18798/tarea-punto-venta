package com.punto.venta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.punto.venta.dto.PedidoDTO;
import com.punto.venta.entity.Cliente;
import com.punto.venta.entity.Pedido;
import com.punto.venta.repository.PedidoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoDTO> listarTodos() {

        return pedidoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO crear(PedidoDTO dto) {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(dto.getIdCliente());

        boolean tienePedidoAbierto =
                pedidoRepository
                        .existsByIdClienteAndEstadoPedidoFalse(cliente);

        if (tienePedidoAbierto) {
            throw new RuntimeException(
                    "El cliente ya tiene un pedido abierto"
            );
        }

        return convertToDTO(
                pedidoRepository.save(
                        convertToEntity(dto)
                )
        );
    }

    public PedidoDTO actualizar(
            Integer idPedido,
            PedidoDTO dto) {

        Pedido pedidoExistente =
                pedidoRepository.findById(idPedido)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Pedido no encontrado"
                                )
                        );

        if (dto.getEstado() != null) {
            pedidoExistente.setEstado(
                    dto.getEstado()
            );
        }

        if (dto.getFechaPedido() != null) {
            pedidoExistente.setFechaPedido(
                    dto.getFechaPedido()
            );
        }

        if (dto.getEstadoPedido() != null) {
            pedidoExistente.setEstadoPedido(
                    dto.getEstadoPedido()
            );
        }

        if (dto.getTotal() != null) {
            pedidoExistente.setTotal(
                    dto.getTotal()
            );
        }

        if (dto.getIdCliente() != null) {

            Cliente cliente = new Cliente();
            cliente.setIdCliente(
                    dto.getIdCliente()
            );

            pedidoExistente.setIdCliente(cliente);
        }

        return convertToDTO(
                pedidoRepository.save(
                        pedidoExistente
                )
        );
    }

    public PedidoDTO anular(
            Integer idPedido,
            PedidoDTO dto) {

        Pedido pedidoExistente =
                pedidoRepository.findById(idPedido)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Pedido no encontrado"
                                )
                        );

        pedidoExistente.setEstado(false);

        return convertToDTO(
                pedidoRepository.save(
                        pedidoExistente
                )
        );
    }

    public void eliminar(Integer idPedido) {

        if (!pedidoRepository.existsById(idPedido)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pedido no encontrado"
            );
        }

        pedidoRepository.deleteById(idPedido);
    }

    private PedidoDTO convertToDTO(Pedido pedido) {

        PedidoDTO dto = new PedidoDTO();

        dto.setIdPedido(
                pedido.getIdPedido()
        );
        dto.setEstado(
                pedido.getEstado()
        );
        dto.setIdCliente(
                pedido.getIdCliente()
                        .getIdCliente()
        );
        dto.setFechaPedido(
                pedido.getFechaPedido()
        );
        dto.setEstadoPedido(
                pedido.getEstadoPedido()
        );
        dto.setTotal(
                pedido.getTotal()
        );

        return dto;
    }

    private Pedido convertToEntity(PedidoDTO dto) {

        Pedido pedido = new Pedido();

        pedido.setIdPedido(
                dto.getIdPedido()
        );
        pedido.setEstado(
                dto.getEstado()
        );

        Cliente cliente = new Cliente();
        cliente.setIdCliente(
                dto.getIdCliente()
        );

        pedido.setIdCliente(cliente);
        pedido.setFechaPedido(
                dto.getFechaPedido()
        );
        pedido.setTotal(
                dto.getTotal()
        );
        pedido.setEstadoPedido(
                dto.getEstadoPedido()
        );

        return pedido;
    }
}
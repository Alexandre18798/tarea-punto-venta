package com.punto.venta.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.punto.venta.dto.PedidoDTO;
import com.punto.venta.entity.Cliente;
import com.punto.venta.entity.Pedido;
import com.punto.venta.repository.ClienteRepository;
import com.punto.venta.repository.PedidoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<PedidoDTO> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO crear(PedidoDTO dto) {

        Cliente cliente = clienteRepository
                .findById(dto.getIdCliente())
                .orElseThrow(() ->
                        new RuntimeException("El cliente no existe"));

        Pedido pedido = new Pedido();

        pedido.setEstado(
                dto.getEstado() != null ? dto.getEstado() : true);

        pedido.setFechaPedido(
                dto.getFechaPedido() != null
                        ? dto.getFechaPedido()
                        : new Date());

        pedido.setEstadoPedido(
                dto.getEstadoPedido() != null
                        ? dto.getEstadoPedido()
                        : true);

        pedido.setTotal(dto.getTotal());
        pedido.setIdCliente(cliente);

        Pedido guardado = pedidoRepository.save(pedido);

        return convertirADTO(guardado);
    }

    private PedidoDTO convertirADTO(Pedido pedido) {

        PedidoDTO dto = new PedidoDTO();

        dto.setIdPedido(pedido.getIdPedido());
        dto.setEstado(pedido.getEstado());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstadoPedido(pedido.getEstadoPedido());
        dto.setTotal(pedido.getTotal());

        if (pedido.getIdCliente() != null) {
            dto.setIdCliente(
                    pedido.getIdCliente().getIdCliente());
        }

        return dto;
    }
}